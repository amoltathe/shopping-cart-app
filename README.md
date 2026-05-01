# shopping-cart-app
A modular, production-style Shopping Cart system built in Java 21, demonstrating Clean Architecture, SOLID principles, and modern concurrency (Virtual Threads).

System Architecture
                    ┌──────────────────────┐
                    │     ProductLoader    │
                    └─────────┬────────────┘
                              │
                 ┌────────────▼────────────┐
                 │  ProductDataSource      │
                 │ (HTTP / Mock / File)    │
                 └────────────┬────────────┘
                              │
                    ┌─────────▼─────────┐
                    │  ProductParser    │
                    └─────────┬─────────┘
                              │
                ┌─────────────▼─────────────┐
                │  ProductRepository (IMM)  │
                └─────────────┬─────────────┘
                              │
                    ┌─────────▼─────────┐
                    │  ProductService   │
                    └─────────┬─────────┘
                              │
                    ┌─────────▼─────────┐
                    │ PriceServiceImpl  │
                    └─────────┬─────────┘
                              │
                    ┌─────────▼─────────┐
                    │ Shopping Cart     │
                    │ Processor         │
                    └─────────┬─────────┘
                              │
                ┌─────────────▼─────────────┐
                │ DefaultTaxService         │
                └─────────────┬─────────────┘
                              │
                    ┌─────────▼─────────┐
                    │ Bill Generator    │
                    └────────────────────┘
⚙️ Key Features
🧾 Product loading from remote JSON
🧠 Immutable repository design
🛒 Cart aggregation logic
💰 Dynamic pricing via service layer
🧾 Tax strategy implementation
📄 Detailed bill breakdown
⚡ Virtual threads for parallel loading
🌐 Modern HttpClient API
🧩 Design Principles
✔ SRP → Each class has single responsibility
✔ DIP → Depends on interfaces
✔ OCP → Extensible tax & pricing logic
✔ ISP → Clean service separation
💰 Sample Output
Cheerios x 2 = 16.86
Cornflakes x 1 = 5.50
--------------------------------
Subtotal: 22.36
Tax (12.5%): 2.79
Total: 25.15

🚀 Tech Stack
Java 21
Maven
JUnit 5
Mockito
HttpClient
Virtual Threads
