package com.at.shopping.cart.service;

public class DefaultTaxService implements TaxService {

    private static final double TAX_RATE = 0.125;

    @Override
    public double calculate(double subtotal) {
        return subtotal * TAX_RATE;
    }
}