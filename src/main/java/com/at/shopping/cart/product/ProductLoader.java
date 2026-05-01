package com.at.shopping.cart.product;
import com.at.shopping.cart.datasource.HttpProductDataSource;
import com.at.shopping.cart.datasource.ProductDataSource;
import com.at.shopping.cart.parser.ProductParser;
import java.io.IOException;
import com.at.shopping.cart.model.Product;
import com.at.shopping.cart.util.RetryExecutor;

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
public final class ProductLoader {

    private static final String BASE_URL =
            "https://equalexperts.github.io/backend-take-home-test-data/";

    private static final String[] FILES = {
            "cheerios.json",
            "cornflakes.json",
            "frosties.json",
            "shreddies.json",
            "weetabix.json"
    };

    private static final int MAX_RETRIES = 3;
    private static final long BACKOFF_MS = 200;
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private ProductLoader() {}

    /**
     * BOOTSTRAP METHOD
     * DESIGN:
     * - Orchestrates parallel loading of product files
     * - Uses virtual threads for scalable I/O
     * - Uses retry utility for resilience
     * - Builds immutable repository as final output
     */
    public static ProductService init() throws IOException {

        ProductDataSource dataSource = HttpProductDataSource
                .builder()
                .build();
        ProductParser parser = new ProductParser();

        List<Product> allProducts = new ArrayList<>();

        // Virtual-thread executor (Java 21)
        try (ExecutorService executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<List<Product>>> futures = new ArrayList<>();

            // Submit parallel tasks
            for (String file : FILES) {
                futures.add(executor.submit(() ->
                        fetchFile(file, dataSource, parser)
                ));
            }

            // Collect results with timeout protection
            for (Future<List<Product>> future : futures) {
                allProducts.addAll(safeGet(future));
            }
        }

        // Immutable repository creation
        ProductRepository repository = ProductRepository.of(allProducts);

        // Service layer wrapper
        return new ProductService(repository);
    }

    /**
     * FETCH + PARSE LAYER
     * DESIGN:
     * - Delegates retry logic to RetryExecutor
     * - Keeps loader clean and focused on orchestration
     */
    private static List<Product> fetchFile(
            String file,
            ProductDataSource dataSource,
            ProductParser parser
    ) {

        String url = BASE_URL + file;

        return RetryExecutor.execute(MAX_RETRIES, BACKOFF_MS, () -> {
            String json = dataSource.fetch(url); // doWork
            return parser.parse(json);
        });
    }


    private static List<Product> safeGet(
            Future<List<Product>> future
    ) throws IOException {
        try {
            return future.get(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
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