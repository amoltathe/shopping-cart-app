package com.at.shopping.cart.model;

public record LineItem(
        String name,
        int quantity,
        double unitPrice,
        double lineTotal
) {

    public String format() {
        return String.format(
                "%.2f × %d = %.2f",
                unitPrice,
                quantity,
                lineTotal
        );
    }
}
