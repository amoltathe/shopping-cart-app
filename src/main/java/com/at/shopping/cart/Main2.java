package com.at.shopping.cart;

import com.at.shopping.cart.core.ShoppingCartProcessor;
import com.at.shopping.cart.product.ProductLoader;
import com.at.shopping.cart.product.ProductService;
import com.at.shopping.cart.service.DefaultTaxService;
import com.at.shopping.cart.service.PriceService;
import com.at.shopping.cart.service.PriceServiceImpl;
import com.at.shopping.cart.service.TaxService;

import java.io.IOException;


public class Main2 {
   //“It’s an IntelliJ inspection based on Java 21 preview rules, but public static void main is still required by the JVM, so I keep it unchanged.”
    public static void main(String[] args) throws IOException {
        // 🏗️ Bootstrap system
        ProductService productService = ProductLoader.init();
        PriceService priceService = new PriceServiceImpl(productService);
        TaxService taxService = new DefaultTaxService();
        ShoppingCartProcessor cart = new ShoppingCartProcessor(priceService,taxService);

        // 📦 ADD ALL PRODUCTS FROM ALL FILES INTO CART
        System.out.println("\n🛒 ADDING ALL PRODUCTS TO CART");
        System.out.println("================================");

        productService.getAllProducts().forEach(product -> {
            cart.addItem(product.getName(), 1);
            System.out.println("Added: " + product.getName());
        });

        // 🛒 CART DETAILS
        System.out.println("\n🛒 CART DETAILS");
        System.out.println("================================");

        cart.getItems().forEach((name, item) ->
                System.out.println(name + " x " + item.getQuantity())
        );

        // 💰 BILL SUMMARY (multi-product breakdown)
        cart.printBillSummary();



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
}
