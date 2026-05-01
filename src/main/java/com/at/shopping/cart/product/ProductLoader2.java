package com.at.shopping.cart.product;

import com.at.shopping.cart.datasource.HttpProductDataSource;
import com.at.shopping.cart.datasource.ProductDataSource;
import com.at.shopping.cart.model.Product;
import com.at.shopping.cart.parser.ProductParser;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * RESPONSIBILITY:
 * Loads product data from remote JSON files, parses them, and builds an immutable repository.
 * DESIGN PRINCIPLES:
 * - Separation of concerns (Loader vs Parser vs Repository)
 * - Fail-fast loading strategy
 * - Parallel I/O using Virtual Threads (Java 21)
 * - Immutable final state (ProductRepository)
 */
public final class ProductLoader2 {

    // Base URL for all product JSON resources
    private static final String BASE_URL =
            "https://equalexperts.github.io/backend-take-home-test-data/";

    // List of external product files to load
    private static final String[] FILES = {
            "cheerios.json",
            "cornflakes.json",
            "frosties.json",
            "shreddies.json",
            "weetabix.json"
    };

    // Retry policy for transient network failures
    private static final int MAX_RETRIES = 3;

    // Timeout protection for each remote fetch
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    // Utility class (no instantiation allowed)
    private ProductLoader2() {}

    /**
     * ENTRY POINT:
     * Initializes the entire product system by:
     * 1. Fetching remote JSON files in parallel
     * 2. Parsing them into domain objects
     * 3. Building an immutable repository
     * DESIGN:
     * - Uses virtual threads for scalable I/O concurrency
     * - Uses try-with-resources for automatic executor cleanup
     * - Ensures fail-safe loading with timeout + exception handling
     */
    public static ProductService init() throws IOException {

        ProductDataSource dataSource = new HttpProductDataSource();
        ProductParser parser = new ProductParser();

        List<Product> allProducts = new ArrayList<>();

        // Virtual-thread executor (lightweight concurrency model)
        try (ExecutorService executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<List<Product>>> futures = new ArrayList<>();

            // Submit one task per file (parallel I/O execution)
            for (String file : FILES) {
                futures.add(executor.submit(() ->
                        fetchWithRetry(file, dataSource, parser)
                ));
            }

            // Collect results with timeout protection
            for (Future<List<Product>> future : futures) {
                try {
                    allProducts.addAll(
                            future.get(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
                    );
                } catch (TimeoutException e) {
                    throw new IOException("Timeout while loading products", e);
                } catch (ExecutionException e) {
                    throw new IOException("Failed to load products", e.getCause());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted while loading products", e);
                }
            }
        }

        // Build immutable repository (single source of truth)
        ProductRepository repository = ProductRepository.of(allProducts);

        // Service layer wraps repository for business logic
        return new ProductService(repository);
    }

    /**
     * NETWORK + PARSE LAYER:
     * Handles fetching and parsing of a single product file.
     * DESIGN:
     * - Retry mechanism for resilience
     * - Backoff strategy for transient failures
     * - Keeps loader logic decoupled from parsing logic
     */

    private static List<Product> fetchWithRetry(
            String file,
            ProductDataSource dataSource,
            ProductParser parser
    ) throws IOException {

        String url = BASE_URL + file;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                // Fetch raw JSON from remote source
                String json = dataSource.fetch(url);

                // Convert JSON → domain objects
                return parser.parse(json);

            } catch (Exception e) {

                // Fail permanently after max retries
                if (attempt == MAX_RETRIES) {
                    throw new IOException("Failed after retries: " + file, e);
                }

                // Simple exponential-like backoff (lightweight)
                try {
                    Thread.sleep(200L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted during retry", ie);
                }
            }
        }

        // Defensive fallback (should never hit)
        throw new IOException("Unreachable code");
    }
}