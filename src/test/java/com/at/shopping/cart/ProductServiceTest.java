package com.at.shopping.cart;


import com.at.shopping.cart.model.Product;
import com.at.shopping.cart.product.ProductRepository;
import com.at.shopping.cart.product.ProductService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    // =========================
    // MOCK DEPENDENCY
    // =========================
    private final ProductRepository repository = mock(ProductRepository.class);
    private final ProductService service = new ProductService(repository);

    // =========================
    // TEST: valid product fetch with normalization
    // =========================
    @Test
    void shouldReturnProductWhenValidNameProvided() {

        Product product = new Product("cheerios", "Cheerios", 8.43);

        when(repository.get("cheerios")).thenReturn(product);

        Product result = service.getProduct("  Cheerios  ");

        assertNotNull(result);
        assertEquals("cheerios", result.getName());

        verify(repository, times(1)).get("cheerios");
    }

    // =========================
    // TEST: null product name
    // =========================
    @Test
    void shouldThrowExceptionWhenNameIsNull() {

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.getProduct(null)
        );

        assertEquals("Product name cannot be null or empty", ex.getMessage());
    }

    // =========================
    // TEST: blank product name
    // =========================
    @Test
    void shouldThrowExceptionWhenNameIsBlank() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getProduct("   ")
        );
    }

    // =========================
    // TEST: get all products
    // =========================
    @Test
    void shouldReturnAllProducts() {

        List<Product> products = List.of(
                new Product("cheerios", "Cheerios", 8.43),
                new Product("cornflakes", "Corn Flakes", 5.50)
        );

        when(repository.getAll()).thenReturn(products);

        List<Product> result = service.getAllProducts();

        assertEquals(2, result.size());
        verify(repository, times(1)).getAll();
    }

    // =========================
    // TEST: verify normalization behavior indirectly
    // =========================
    @Test
    void shouldNormalizeInputBeforeCallingRepository() {

        Product product = new Product("frosties", "Frosties", 4.20);

        when(repository.get("frosties")).thenReturn(product);

        service.getProduct("FROST IES");

        verify(repository).get("frosties");
    }
}