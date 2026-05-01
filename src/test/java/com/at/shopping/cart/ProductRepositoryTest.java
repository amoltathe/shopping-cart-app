package com.at.shopping.cart;


import com.at.shopping.cart.model.Product;
import com.at.shopping.cart.product.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    // =========================
    // TEST DATA SETUP
    // =========================
    private List<Product> sampleProducts() {
        return List.of(
                new Product("cheerios", "Cheerios", 8.43),
                new Product("cornflakes", "Corn Flakes", 5.50),
                new Product("frosties", "Frosties", 4.20)
        );
    }

    // =========================
    // TEST: repository creation
    // =========================
    @Test
    void shouldCreateRepositoryFromProductList() {

        ProductRepository repo = ProductRepository.of(sampleProducts());

        assertNotNull(repo);
        assertEquals(3, repo.getAll().size());
    }

    // =========================
    // TEST: get product by name (case + space insensitive)
    // =========================
    @Test
    void shouldGetProductIgnoringCaseAndSpaces() {

        ProductRepository repo = ProductRepository.of(sampleProducts());

        Product product = repo.get("  Cheerios  ");

        assertEquals("cheerios", product.getName());
        assertEquals("Cheerios", product.getTitle());
        assertEquals(8.43, product.getPrice());
    }

    // =========================
    // TEST: product exists
    // =========================
    @Test
    void shouldReturnTrueIfProductExists() {

        ProductRepository repo = ProductRepository.of(sampleProducts());

        assertTrue(repo.exists("CORNFLAKES"));
        assertTrue(repo.exists(" corn flakes "));
    }

    // =========================
    // TEST: product not found
    // =========================
    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        ProductRepository repo = ProductRepository.of(sampleProducts());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> repo.get("unknown")
        );

        assertTrue(ex.getMessage().contains("Product not found"));
    }

    // =========================
    // TEST: immutability check
    // =========================
    @Test
    void shouldBeImmutable() {

        ProductRepository repo = ProductRepository.of(sampleProducts());

        assertThrows(
                UnsupportedOperationException.class,
                () -> repo.getAll().add(
                        new Product("x", "X", 1.0)
                )
        );
    }

    // =========================
    // TEST: duplicate handling (existing wins)
    // =========================
    @Test
    void shouldIgnoreDuplicateProducts() {

        List<Product> duplicates = List.of(
                new Product("cheerios", "Cheerios A", 8.0),
                new Product("cheerios", "Cheerios B", 9.0)
        );

        ProductRepository repo = ProductRepository.of(duplicates);

        Product product = repo.get("cheerios");

        // existing wins due to merge rule
        assertEquals(8.0, product.getPrice());
    }
}