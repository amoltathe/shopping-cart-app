package com.at.shopping.cart;

import com.at.shopping.cart.core.ShoppingCartProcessor;
import com.at.shopping.cart.product.ProductLoader;
import com.at.shopping.cart.product.ProductService;
import com.at.shopping.cart.service.DefaultTaxService;
import com.at.shopping.cart.service.PriceService;
import com.at.shopping.cart.service.PriceServiceImpl;
import com.at.shopping.cart.service.TaxService;

import java.io.IOException;
import java.util.Scanner;

public class Main3 {


    static void main(String[] args) throws IOException {

        var productService = ProductLoader.init();
        PriceService priceService = new PriceServiceImpl(productService);
        TaxService taxService = new DefaultTaxService();
        ShoppingCartProcessor cart = new ShoppingCartProcessor(priceService, taxService);

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n🛒 WELCOME TO SHOPPING CART");

        productService.getAllProducts()
                .forEach(p -> System.out.println("- " + p.getName()));

        while (true) {

            System.out.print("\nEnter product name (or done/exit): ");
            String productName = scanner.nextLine().trim();

            if (isExit(productName)) break;

            if (productName.isEmpty()) {
                System.out.println("⚠ Product cannot be empty.");
                continue;
            }

            if (!isValidProduct(productName, productService)) {
                System.out.println("⚠ Invalid product.");
                continue;
            }

            int quantity = readQuantity(scanner);

            if (quantity == -1) {
                break;
            }

            cart.addItem(productName, quantity);
            System.out.println("✅ Added: " + productName + " x " + quantity);
        }

        System.out.println("\n🛒 FINAL CART");
        cart.getItems().values().forEach(item ->
                System.out.println(item.getProductName() + " x " + item.getQuantity())
        );

        cart.printBillSummary();

        scanner.close();
    }
    private static boolean isExit(String input) {
        return input.equalsIgnoreCase("done")
                || input.equalsIgnoreCase("exit");
    }
    private static boolean isValidProduct(String productName, ProductService productService) {
        return productService.getAllProducts()
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(productName));
    }
    private static int readQuantity(Scanner scanner) {

        while (true) {

            int qty = readInt(scanner);

            if (qty == -1) {
                return -1;
            }

            if (qty <= 0) {
                System.out.println("⚠ Quantity must be greater than 0.");
                continue;
            }

            return qty;
        }
    }
    private static int readInt(Scanner scanner) {

        while (true) {

            //System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (isExit(input)) {
                return -1;
            }

            try {
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {
                System.out.println("⚠ Invalid number. Please enter a valid integer.");
            }
        }
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

