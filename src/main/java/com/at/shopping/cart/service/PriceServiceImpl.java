package com.at.shopping.cart.service;

import com.at.shopping.cart.product.ProductService;

public class PriceServiceImpl implements  PriceService{


    private final ProductService productService;

    public PriceServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public double getPrice(String productName) {
        return productService
                .getProduct(productName)
                .getPrice();
    }
}
