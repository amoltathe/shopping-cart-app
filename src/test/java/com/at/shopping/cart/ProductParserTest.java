package com.at.shopping.cart;


import com.at.shopping.cart.model.Product;
import com.at.shopping.cart.parser.ProductParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductParserTest {

    private final ProductParser parser = new ProductParser();

    // =========================
    // ✅ VALID JSON TEST
    // =========================
    @Test
    void shouldParseValidProductJson() {

        String json = """
                {
                  "title": "Cheerios",
                  "price": 8.43
                }
                """;

        List<Product> result = parser.parse(json);

        assertNotNull(result);
        assertEquals(1, result.size());

        Product product = result.get(0);

        assertEquals("cheerios", product.getName());
        assertEquals("Cheerios", product.getTitle());
        assertEquals(8.43, product.getPrice());
    }

    // =========================
    // ❌ INVALID JSON STRUCTURE
    // =========================
    @Test
    void shouldThrowExceptionForInvalidJsonStructure() {

        String json = """
                {
                  "invalid": "data"
                }
                """;

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> parser.parse(json)
        );

        assertTrue(ex.getMessage().contains("Failed to parse product JSON"));
    }

    // =========================
    // ❌ MISSING TITLE
    // =========================
    @Test
    void shouldThrowExceptionWhenTitleMissing() {

        String json = """
                {
                  "price": 10.0
                }
                """;

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> parser.parse(json)
        );

        assertTrue(ex.getMessage().contains("Failed to parse product JSON"));
    }

    // =========================
    // ❌ MISSING PRICE
    // =========================
    @Test
    void shouldThrowExceptionWhenPriceMissing() {

        String json = """
                {
                  "title": "Cornflakes"
                }
                """;

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> parser.parse(json)
        );

        assertTrue(ex.getMessage().contains("Failed to parse product JSON"));
    }

    // =========================
    // ❌ NULL INPUT
    // =========================
    @Test
    void shouldThrowExceptionForNullInput() {

        assertThrows(
                RuntimeException.class,
                () -> parser.parse(null)
        );
    }
}