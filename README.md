# Java Challenge Calculator
## Description
A modular Java calculator application built for the WIT Java challenge.
It contains:
- **calculator** module — calculation engine and Kafka request consumer
- **rest** module — REST API that exposes calculator endpoints and communicates via Kafka
- **Kafka** — used for inter-module request/response communication

Supports arbitrary precision signed decimal numbers using `BigDecimal`.

---

## Modules
- **calculator** — Core calculation logic and Kafka listener
- **rest** — REST API controller + Kafka gateway
- **common** — Shared request/response models

---

## Requirements
- Java 17+
- Maven 3.8+
- Spring Boot 3.2+
- Apache Kafka
- Docker + Docker Compose

---

## Build
Build all modules:
```bash
mvn clean install
```

## Run (Docker)
Start Kafka + calculator + rest:
```bash
docker compose up --build
```

Test the API:
```bash
curl "http://localhost:8080/sum?a=1&b=2"
curl "http://localhost:8080/subtraction?a=5&b=3"
curl "http://localhost:8080/multiplication?a=2&b=4"
curl "http://localhost:8080/division?a=10&b=2"
```

## REST API Endpoints
All endpoints use query parameters a and b.

- GET /sum?a={a}&b={b}
- GET /subtraction?a={a}&b={b}
- GET /multiplication?a={a}&b={b}
- GET /division?a={a}&b={b}

Example:
```bash
curl "http://localhost:8080/sum?a=1&b=2"
```

Response:
```json
{
  "result": 3
}
```

## Error Handling
- 400 Bad Request — missing required parameters
- 500 Internal Server Error — calculation failure (e.g. division by zero)

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