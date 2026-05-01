package com.at.shopping.cart.parser;

import com.at.shopping.cart.model.Product;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ProductParser {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<Product> parse(String json) {
        try {
            JsonNode root = mapper.readTree(json);

            if (root.isMissingNode() || !root.isObject()) {
                throw new IllegalArgumentException("Invalid JSON: expected product object");
            }

            String title = root.path("title").asText(null);
            double price = root.path("price").asDouble(Double.NaN);

            if (title == null || Double.isNaN(price)) {
                throw new IllegalArgumentException("Missing required fields");
            }
            return List.of(new Product(getName(title), title, price));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse product JSON", e);
        }
    }

    private String getName(String title) {
        return java.util.Objects.toString(title, "")
                .toLowerCase()
                .replaceAll("\\s+", "");
    }
}