package com.at.shopping.cart.product;

import com.at.shopping.cart.model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProductRepository {

    private final Map<String, Product> productMap;

    // Private constructor (force controlled creation)
    private ProductRepository(Map<String, Product> productMap) {
        this.productMap = Map.copyOf(productMap); // immutable
    }

    // Factory method (recommended)
    //Factory pattern (clean construction)
    public static ProductRepository of(List<Product> products) {
        Map<String, Product> map = products.stream()
                .collect(Collectors.toMap(
                        p -> normalize(p.getName()),
                        Function.identity(),
                        (existing, replacement) -> existing // handle duplicates
                ));

        return new ProductRepository(map);
    }

    public Product get(String name) {
        Product product = productMap.get(normalize(name));

        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + name);
        }

        return product;
    }

    public boolean exists(String name) {
        return productMap.containsKey(normalize(name));
    }

    public List<Product> getAll() {
        return List.copyOf(productMap.values());
    }

    // 🔥 Fast + safe normalization
    private static String normalize(String value) {
        if (value == null) return "";

        StringBuilder sb = new StringBuilder(value.length());
        for (char c : value.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

}
