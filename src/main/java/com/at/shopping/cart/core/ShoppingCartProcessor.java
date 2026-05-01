package com.at.shopping.cart.core;

import com.at.shopping.cart.model.CartItem;
import com.at.shopping.cart.model.LineItem;
import com.at.shopping.cart.service.PriceService;
import com.at.shopping.cart.service.TaxService;

import java.util.*;

/**
 * ShoppingCartProcessor is responsible for:
 * - Maintaining cart state
 * - Calculating subtotal
 * - Delegating pricing to PriceService
 * - Delegating tax calculation to TaxService
 * - Building bill representation (LineItem)
 * - Printing final bill (presentation layer)
 * Follows SOLID principles:
 * - SRP: each service has a single responsibility
 * - DIP: depends on abstractions (PriceService, TaxService)
 */
public class ShoppingCartProcessor {
    /*
     * Internal cart storage:
     * productName → CartItem (quantity tracking)
     */
    private final Map<String, CartItem> items = new HashMap<>();

    /**
     * External dependency for pricing logic
     * (DIP - depends on abstraction, not implementation)
     */
    private final PriceService priceService;

    /**
     * External dependency for tax calculation logic
     * (supports different tax strategies if needed)
     */
    private final TaxService taxService;

    /**
     * Constructor injection ensures immutability of dependencies
     */
    public ShoppingCartProcessor(PriceService priceService, TaxService taxService) {
        this.priceService = priceService;
        this.taxService = taxService;
    }

    /**
     * Adds item to cart.
     * If item already exists → increases quantity.
     * Example:
     * addItem("cheerios", 2)
     */
    public void addItem(String productName, int quantity) {
        items.computeIfAbsent(productName, _ -> new CartItem(productName, 0))
                .addQuantity(quantity);
    }

    /**
     * Returns read-only view of cart items.
     * Prevents external modification of internal state.
     */
    public Map<String, CartItem> getItems() {
        return Collections.unmodifiableMap(items);
    }

    /**
     * Calculates subtotal:
     * sum(price × quantity) for all items
     * Delegates pricing responsibility to PriceService
     */
    public double getSubtotal() {
        double subtotal = 0.0;

        for (CartItem item : items.values()) {
            double price = priceService.getPrice(item.getProductName());
            subtotal += price * item.getQuantity();
        }

        return round(subtotal);
    }

    /**
     * Tax calculation delegated to TaxService.
     * This allows:
     * - different tax rules per country
     * - future strategy changes without modifying cart
     */
    public double getTax() {
        return round(taxService.calculate(getSubtotal()));
    }

    /**
     * Final total = subtotal + tax
     */
    public double getTotal() {
        return round(getSubtotal() + getTax());
    }

    /**
     * Builds structured billing data.
     * LineItem represents:
     * - product name
     * - quantity
     * - total price per product
     * This can be reused for:
     * - REST APIs
     * - invoices
     * - UI rendering
     */
    public List<LineItem> getBillLines() {

        List<LineItem> lines = new ArrayList<>();

        for (CartItem item : items.values()) {

            double price = priceService.getPrice(item.getProductName());
            double lineTotal = price * item.getQuantity();

            lines.add(new LineItem(
                    item.getProductName(),
                    item.getQuantity(),
                    price,
                    round(lineTotal)
            ));
        }

        return lines;
    }

    /**
     * Prints final bill summary.
     * IMPORTANT:
     * - Only presentation logic here
     * - No business rules inside printing layer
     */
    public void printBillSummary() {


        System.out.println("\n💰 BILL SUMMARY");
        System.out.println("================================\n");

        for (LineItem line : getBillLines()) {
            System.out.printf("%-12s %.2f × %d = %.2f%n",
                    line.name(),
                    line.unitPrice(),
                    line.quantity(),
                    line.lineTotal()
            );
        }

        double subtotal = getSubtotal();
        double tax = getTax();
        double total = getTotal();

        System.out.println("\n--------------------------------");
        System.out.printf("%-15s %.2f%n", "Subtotal:", subtotal);
        System.out.printf("%-15s %.2f%n", "Tax (12.5%):", tax);
        System.out.println("--------------------------------");
        System.out.printf("%-15s %.2f%n", "Total:", total);
        System.out.println("================================");
    }

    /**
     * Utility method for rounding to 2 decimal places.
     */
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}