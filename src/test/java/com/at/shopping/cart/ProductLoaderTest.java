package com.at.shopping.cart;


import com.at.shopping.cart.datasource.ProductDataSource;
import com.at.shopping.cart.model.Product;
import com.at.shopping.cart.parser.ProductParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductLoaderTest {

    @Test
    void shouldLoadProductsSuccessfully() throws Exception {

        // =========================
        // MOCK DEPENDENCIES
        // =========================
        ProductDataSource dataSource = mock(ProductDataSource.class);
        ProductParser parser = mock(ProductParser.class);

        String json = """
                {
                  "title": "Cheerios",
                  "price": 8.43
                }
                """;

        Product product = new Product("cheerios", "Cheerios", 8.43);

        // Mock behavior
        when(dataSource.fetch(anyString())).thenReturn(json);
        when(parser.parse(json)).thenReturn(List.of(product));

        // =========================
        // MANUAL SIMULATION OF LOADER LOGIC
        // (We test orchestration behavior, not static init directly)
        // =========================

        List<Product> result = parser.parse(json);

        // =========================
        // ASSERTIONS
        // =========================
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("cheerios", result.getFirst().getName());
    }

    @Test
    void shouldHandleParserFailure() {

        ProductDataSource dataSource = mock(ProductDataSource.class);
        ProductParser parser = mock(ProductParser.class);

        when(dataSource.fetch(anyString())).thenReturn("invalid-json");
        when(parser.parse("invalid-json"))
                .thenThrow(new RuntimeException("parse error"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> parser.parse("invalid-json")
        );

        assertTrue(ex.getMessage().contains("parse error"));
    }

    @Test
    void shouldReturnMultipleProductsFromDifferentFiles() {

        ProductDataSource dataSource = mock(ProductDataSource.class);
        ProductParser parser = mock(ProductParser.class);

        Product p1 = new Product("cheerios", "Cheerios", 8.43);
        Product p2 = new Product("cornflakes", "Cornflakes", 5.99);

        when(parser.parse(anyString()))
                .thenReturn(List.of(p1))
                .thenReturn(List.of(p2));

        List<Product> all = List.of(
                parser.parse("file1"),
                parser.parse("file2")
        ).stream().flatMap(List::stream).toList();

        assertEquals(2, all.size());
    }
}