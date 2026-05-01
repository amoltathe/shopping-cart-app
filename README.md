# shopping-cart-app
A modular, production-style Shopping Cart system built in Java 21, demonstrating Clean Architecture, SOLID principles, and modern concurrency (Virtual Threads).

✔ Clean Architecture
✔ SOLID principles
✔ Modern concurrency (Virtual Threads)
✔ Testable design with dependency injection

📌 Overview
<details> <summary>Click to expand</summary>

This system simulates a real-world e-commerce shopping cart:

📦 Loads product data from remote JSON sources
🧠 Parses and stores products in an immutable repository
🛒 Supports cart operations (add items, quantity aggregation)
💰 Calculates subtotal, tax, and total
🧾 Generates detailed bill breakdown
⚡ Uses Java 21 features like Virtual Threads + HttpClient
</details>
🏗️ System Architecture
<details> <summary>Click to expand architecture diagram</summary>
ProductLoader
     ↓
ProductDataSource (HTTP / Mock / File)
     ↓
ProductParser
     ↓
ProductRepository (Immutable)
     ↓
ProductService
     ↓
PriceServiceImpl
     ↓
ShoppingCartProcessor
     ↓
DefaultTaxService
     ↓
Bill Generator
</details>
🔌 Core Components
📦 ProductDataSource
<details> <summary>Abstraction Layer</summary>
public interface ProductDataSource {
    String fetch(String endpoint);
}

✔ Decouples data source
✔ Supports HTTP / File / Mock
✔ Follows DIP principle

</details>
🌐 HTTP Data Source
<details> <summary>Modern HttpClient implementation</summary>

✔ Uses Java 21 HttpClient
✔ Builder-based request creation
✔ Fully testable

</details>
📦 Product Repository
<details> <summary>Immutable storage layer</summary>

✔ Immutable Map storage
✔ Case-insensitive lookup
✔ Factory method (of)

</details>
🧠 Product Service
<details> <summary>Domain access layer</summary>

✔ Wraps repository
✔ Central product lookup point
✔ Hides storage complexity

</details>
💰 Price Service
<details> <summary>Adapter layer for pricing</summary>
public class PriceServiceImpl implements PriceService {

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

✔ Decouples pricing from cart
✔ Adapter pattern
✔ DIP-compliant

</details>
🧾 Tax Service
<details> <summary>Strategy implementation</summary>
public class DefaultTaxService implements TaxService {

    private static final double TAX_RATE = 0.125;

    @Override
    public double calculate(double subtotal) {
        return subtotal * TAX_RATE;
    }
}

✔ Strategy pattern
✔ Easily replaceable tax rules

</details>
🛒 Shopping Cart Engine
<details> <summary>Core cart logic</summary>

✔ Add items with quantity
✔ Auto aggregation
✔ Subtotal calculation
✔ Tax + total calculation
✔ LineItem-based bill generation

</details>
💰 Sample Output
Cheerios x 2 = 16.86
Cornflakes x 1 = 5.50
--------------------------------
Subtotal: 22.36
Tax (12.5%): 2.79
Total: 25.15
⚡ Java 21 Features
<details> <summary>Click to expand</summary>
🧵 Virtual Threads (parallel product loading)
🌐 HttpClient API (modern HTTP calls)
📦 Immutable collections
⚙ Functional streams
🧠 Builder pattern usage
</details>
🧩 Design Principles
<details> <summary>SOLID Principles</summary>

✔ SRP → Each class has single responsibility
✔ DIP → Uses interfaces for dependencies
✔ OCP → Extensible tax/pricing logic
✔ ISP → Clean service separation

</details>
📂 Project Structure
<details> <summary>Click to expand structure</summary>
com.at.shopping.cart
│
├── core
│   └── ShoppingCartProcessor
│
├── datasource
│   ├── ProductDataSource
│   └── HttpProductDataSource
│
├── parser
│   └── ProductParser
│
├── product
│   ├── ProductLoader
│   ├── ProductRepository
│   └── ProductService
│
├── service
│   ├── PriceService
│   ├── PriceServiceImpl
│   ├── TaxService
│   └── DefaultTaxService
│
└── model
    ├── Product
    ├── CartItem
    └── LineItem
</details>
🚀 How to Run
mvn clean install
java -cp target/app.jar com.at.shopping.cart.Main
🧪 Testing
<details> <summary>Testing stack</summary>
JUnit 5
Mockito

✔ Service layer tests
✔ Repository tests
✔ Cart calculation tests
✔ HTTP datasource mocking

</details>
🔥 Key Highlights
🧠 Clean Architecture design
⚡ Virtual-thread parallel loading
🧾 Structured billing system
🧩 Fully testable service layer
🔌 Highly extensible design
🌐 Modern HTTP integration
📈 Future Enhancements
💸 Discount/Coupon engine
🧠 Redis caching layer
🌐 Spring Boot REST API
🗄 Database persistence (JPA)
⚡ Async structured concurrency
👨‍💻 Summary

This project demonstrates:

✔ Production-level Java design
✔ Strong SOLID principles
✔ Clean separation of concerns
✔ Scalable system architecture
✔ Modern Java 21 usage
