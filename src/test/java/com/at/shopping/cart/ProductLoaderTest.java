package com.at.shopping.cart;

import com.at.shopping.cart.datasource.ProductDataSource;
import com.at.shopping.cart.model.Product;
import com.at.shopping.cart.product.ProductLoader;
import com.at.shopping.cart.product.ProductService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductLoaderTest {

    /**
     * Fake DataSource that bypasses HTTP but keeps architecture intact
     */
    static class FakeProductDataSource implements ProductDataSource {

        @Override
        public String fetch(String endpoint) {

            if (endpoint.contains("cheerios")) {
                return """
                {
                  "title": "Cheerios",
                  "price": 4.68
                }
                """;
            }

            if (endpoint.contains("cornflakes")) {
                return """
                {
                  "title": "Cornflakes",
                  "price": 2.52
                }
                """;
            }

            return """
            {
              "title": "Unknown",
              "price": 1.00
            }
            """;
        }
    }

    /**
     * Helper method to create service with fake dependency
     */
    private ProductService createService() throws IOException {
        return ProductLoader.init(new FakeProductDataSource());
    }

    /**
     * TEST: Full product loading flow
     */
    @Test
    void shouldLoadProductsSuccessfully() throws IOException {

        ProductService service = createService();

        List<Product> products = service.getAllProducts();

        assertNotNull(products);
        assertFalse(products.isEmpty());

        assertTrue(
                products.stream()
                        .anyMatch(p -> p.getTitle().equals("Cheerios"))
        );
    }

    /**
     * TEST: Validate product correctness
     */
    @Test
    void shouldMatchProductValues() throws IOException {

        ProductService service = createService();

        Product cheerios = service.getAllProducts()
                .stream()
                .filter(p -> p.getTitle().equals("Cheerios"))
                .findFirst()
                .orElse(null);

        assertNotNull(cheerios);
        assertEquals(4.68, cheerios.getPrice());
    }

    /**
     * TEST: Ensure system handles unknown product safely
     */
    @Test
    void shouldHandleUnknownProduct() throws IOException {

        ProductService service = createService();

        assertNotNull(service.getAllProducts());
        assertFalse(service.getAllProducts().isEmpty());
    }
}