package com.at.shopping.cart.product;

import com.at.shopping.cart.model.Product;

import java.util.List;

//ProductService (FINAL CLEAN FACTORY REPLACEMENT)
public class ProductService {


    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    /**
     * Fetch single product by name.
     * Normalizes input to avoid case mismatch issues.
     */
    public Product getProduct(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }

        return repository.get(normalize(name));
    }

    /**
     * Returns all available products.
     * Useful for loading cart with all FILES data or reporting.
     */
    public List<Product> getAllProducts() {
        return repository.getAll();
    }

    /**
     * Normalizes product names for consistent lookup.
     */
    private String normalize(String name) {
        return name.toLowerCase().replaceAll("\\s+", "");
    }


}
