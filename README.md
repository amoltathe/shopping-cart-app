# shopping-cart-app
A modular, production-style Shopping Cart system built in Java 21, demonstrating Clean Architecture, SOLID principles, and modern concurrency (Virtual Threads).
✔ Clean Architecture
✔ SOLID principles
✔ Modern concurrency (Virtual Threads)
✔ Testable design with dependency injection

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

Uses Java HttpClient (no deprecated APIs).

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

✔ Production-level Java design
✔ Strong SOLID principles
✔ Clean separation of concerns
✔ Scalable system architecture
✔ Modern Java 21 usage
