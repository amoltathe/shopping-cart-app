package com.at.shopping.cart;

import com.at.shopping.cart.core.ShoppingCartProcessor;
import com.at.shopping.cart.datasource.HttpProductDataSource;
import com.at.shopping.cart.service.DefaultTaxService;
import com.at.shopping.cart.service.PriceService;
import com.at.shopping.cart.service.PriceServiceImpl;
import com.at.shopping.cart.product.ProductLoader;
import com.at.shopping.cart.product.ProductService;
import com.at.shopping.cart.service.TaxService;

import java.io.IOException;
import java.util.Scanner;


public class Main {

    static void main() throws IOException {
        // Bootstrap system
        ProductService productService = ProductLoader.init(HttpProductDataSource.builder().build());
        PriceService priceService = new PriceServiceImpl(productService);
        TaxService taxService = new DefaultTaxService();
        ShoppingCartProcessor cart = new ShoppingCartProcessor(priceService, taxService);

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n🛒 WELCOME TO SHOPPING CART");
        System.out.println("==============================");

        System.out.println("\nAvailable Products:");
        productService.getAllProducts()
                .forEach(p -> System.out.println("- " + p.getName()));

        // 📥 INPUT LOOP
        while (true) {

            String productName = readProduct(scanner);

            if (isExit(productName)) {
                break;
            }

            if (isInvalidProduct(productName, productService)) {
                System.out.println("⚠ Invalid product. Try again.");
                continue;
            }

            int quantity = readQuantity(scanner);

            if (quantity == -1) {
                break;
            }

            cart.addItem(productName, quantity);
            System.out.println("✅ Added: " + productName + " x " + quantity);
        }

        // 🛒 CART DETAILS
        printCart(cart);

        // 💰 BILL SUMMARY
        cart.printBillSummary();

        scanner.close();
    }

    // =========================
    // 🔧 Helper Methods
    // =========================

    private static String readProduct(Scanner scanner) {
        System.out.print("\nEnter product name (or done): ");
        return scanner.nextLine().trim();
    }

    private static boolean isExit(String input) {
        return input.equalsIgnoreCase("done")
                || input.equalsIgnoreCase("exit");
    }

    private static boolean isInvalidProduct(String productName, ProductService productService) {
        return productName.isEmpty() ||
                productService.getAllProducts()
                        .stream()
                        .noneMatch(p -> p.getName().equalsIgnoreCase(productName));
    }

    private static int readQuantity(Scanner scanner) {

        while (true) {

            System.out.print("Enter quantity (or done/exit): ");
            String input = scanner.nextLine().trim();

            if (isExit(input)) {
                return -1;
            }

            try {
                int qty = Integer.parseInt(input);

                if (qty <= 0) {
                    System.out.println("⚠ Quantity must be greater than 0.");
                    continue;
                }

                return qty;

            } catch (NumberFormatException e) {
                System.out.println("⚠ Invalid quantity. Please enter a number.");
            }
        }
    }

    private static void printCart(ShoppingCartProcessor cart) {

        System.out.println("\n🛒 CART DETAILS");
        System.out.println("==============================");

        cart.getItems().values().forEach(item ->
                System.out.println(item.getProductName() + " x " + item.getQuantity())
        );
    }
}


    /*


    shopping-cart/
│
├── src/
│   ├── main/java/com/example/cart/
│   │
│   │   ├── app/
│   │   │   └── Main.java
│   │   │
│   │   ├── core/
│   │   │   └── ShoppingCart.java
│   │   │
│   │   ├── service/
│   │   │   ├── PriceService.java
│   │   │   └── PriceServiceImpl.java
│   │   │
│   │   ├── product/
│   │   │   ├── Product.java
│   │   │   ├── ProductService.java
│   │   │   ├── ProductLoader.java
│   │   │   └── ProductRepository.java
│   │   │
│   │   ├── parser/
│   │   │   └── ProductParser.java
│   │   │
│   │   ├── datasource/
│   │   │   ├── ProductDataSource.java
│   │   │   └── HttpProductDataSource.java
│   │
│   └── test/java/com/example/cart/
│       ├── ShoppingCartTest.java
│       ├── PriceServiceTest.java
│       ├── ProductLoaderTest.java
│       └── ProductServiceTest.java
│
├── pom.xml
└── README.md

     */

