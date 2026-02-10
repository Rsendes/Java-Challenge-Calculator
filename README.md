# Java Challenge Calculator
## Description
A modular Java calculator application with:
- Core calculation engine `calculator` module)
- REST API `rest` module)
- Kafka-based request/response gateway
Supports operations like sum, subtraction, multiplication, division, with proper error handling (e.g., division by zero returns 500).
## Modules
- **calculator** – Core logic and services  
- **rest** – REST API controller exposing calculator endpoints  
- **common** – Shared classes and Kafka request/response models (included in both modules)
## Requirements
- Java 17+  
- Maven 3.8+  
- Spring Boot 3.2+  
- Kafka (for async request/response)  
## Docker (Optional)
You can optionally run the application with Docker and Docker Compose:
1. Build the module jars:
```bash
mvn clean package
```
2. Run all services with Kafka using Docker Compose:
```bash
docker compose up --build
```
## Build
Build all modules:
```bash
mvn clean install
```
Build and test only a specific module:
```bash
mvn test -pl rest
mvn test -pl calculator
```
## Run
Run modules individually:
```bash
mvn spring-boot:run -pl calculator
mvn spring-boot:run -pl rest
```
REST service runs on port 8080
Calculator service runs on port 8081
## API Endpoints
GET /sum?a={num1}&b={num2} – Returns sum of a and b
GET /subtraction?a={num1}&b={num2} – Returns subtraction a - b
GET /multiplication?a={num1}&b={num2} – Returns multiplication a * b
GET /division?a={num1}&b={num2} – Returns division a / b
Responses:
400 Bad Request – Missing parameter
500 Internal Server Error – Division by zero
## Testing
Run all tests:
```bash
mvn test
```
Run tests for a single module:
```bash
mvn test -pl calculator
mvn test -pl rest
```