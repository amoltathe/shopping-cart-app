# shopping-cart-app
A modular, production-style Shopping Cart system built in Java 21, demonstrating Clean Architecture, SOLID principles, and modern concurrency (Virtual Threads).

<pre>
✔ Clean Architecture
✔ SOLID principles
✔ Modern concurrency (Virtual Threads)
✔ Testable design with dependency injection
</pre>
# Navigation
<pre>
1. Overview
2. Architecture
3. Core Components
4. Project Structure
5. How to Run
6. Testing
7. Key Highlights
8. Future Enhancements
</pre>
# Overview
<pre>
A modular Shopping Cart system built using Java 21 that demonstrates:
Clean Architecture
SOLID principles
Virtual Threads
Extensible service-based design
</pre>
# Architecture
<pre> ProductLoader ↓ ProductDataSource ↓ ProductParser ↓ ProductRepository ↓ ProductService ↓ PriceServiceImpl ↓ ShoppingCartProcessor ↓ DefaultTaxService ↓ Bill Generator </pre>

# Core Components
<pre>
📦 ProductDataSource

Abstracts data fetching (HTTP / File / Mock).

🌐 HttpProductDataSource

Uses Java HttpClient.

📦 ProductRepository

Immutable in-memory storage with fast lookup.

🧠 ProductService

Domain layer over repository.

💰 PriceServiceImpl

Adapter between ProductService and pricing logic.

🧾 DefaultTaxService

Strategy-based tax calculation.

🛒 ShoppingCartProcessor
</pre>

Core engine for cart + billing logic.

 # Project Structure
<pre>
com.at.shopping.cart
│
├── core
│   └── ShoppingCartProcessor.java
│
├── datasource
│   ├── ProductDataSource.java
│   └── HttpProductDataSource.java
│
├── parser
│   └── ProductParser.java
│
├── product
│   ├── ProductLoader.java
│   ├── ProductRepository.java
│   └── ProductService.java
│
├── service
│   ├── PriceService.java
│   ├── PriceServiceImpl.java
│   ├── TaxService.java
│   └── DefaultTaxService.java
│
├── model
│   ├── Product.java
│   ├── CartItem.java
│   └── LineItem.java
│
└── util
    └── RetryExecutor.java
</pre> 
# How to Run
mvn clean install
java -cp target/app.jar com.at.shopping.cart.Main

# Sample Output
<pre>
🛒 WELCOME TO SHOPPING CART
==============================

Available Products:
- frosties
- weetabix
- cornflakes
- shreddies
- cheerios

Enter product name (or done): frosties
Enter quantity (or done/exit): 2
✅ Added: frosties x 2

Enter product name (or done): done

🛒 CART DETAILS
==============================
frosties x 2

 BILL SUMMARY
================================

frosties     4.99 × 2 = 9.98

--------------------------------
Subtotal:       9.98
Tax (12.5%):    1.25
--------------------------------
Total:          11.23
================================
</pre>

# Testing
<pre>

JUnit 5
Mockito
Mocked HTTP layer
Service-level unit tests
</pre>
# Key Highlights
⚡ Virtual Threads for parallel loading
🧠 Clean layered architecture
🔌 Fully decoupled services
🧾 Structured billing system
🧪 Highly testable design

# Future Enhancements
<pre>

💸 Discount engine
🌐 Spring Boot REST API
🧠 Redis caching layer
🗄 Database persistence
⚡ Async structured concurrency
</pre>
# Project Details
<pre>
This project is a Java 21-based Shopping Cart system designed using Clean Architecture and SOLID principles.

At the top, I have a ProductLoader which loads product data from remote JSON sources using a ProductDataSource abstraction. This allows me to switch between HTTP, file, or mock implementations without changing core logic.

The raw JSON is parsed using ProductParser and stored in an immutable ProductRepository, ensuring thread safety and consistency.

On top of that, ProductService acts as a domain layer, and PriceServiceImpl adapts product data into pricing logic. This separation ensures pricing logic is decoupled from product storage.

The ShoppingCartProcessor is the core engine. It handles cart state, quantity aggregation, subtotal calculation, and delegates pricing and tax logic to dedicated services.

Tax calculation is handled using DefaultTaxService, which follows a strategy pattern so tax rules can be changed without modifying cart logic.

Finally, I generate a LineItem-based bill breakdown, separating business logic from presentation.

From a Java perspective, I also used modern features like HttpClient, Virtual Threads for parallel product loading, and immutable collections for safety.

Overall, the system is designed to be scalable, testable, and production-ready with clear separation of concerns.

✔ Production-level Java design
✔ Strong SOLID principles
✔ Clean separation of concerns
✔ Scalable system architecture
✔ Modern Java 21 usage
</pre>

