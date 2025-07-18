
# Calculator REST API with Kafka Integration

## Overview

This project demonstrates a microservices-based calculator application built with Java Spring Boot, Docker, and Apache Kafka. It consists of two main modules:

- **calculator-rest**: Exposes RESTful APIs to perform arithmetic operations.
- **calculator-core**: Listens to Kafka topics, performs calculations, and handles the core business logic.

The inter-module communication is managed asynchronously through Kafka message streaming.

## Project Structure

```
calculator-rest
 ├── src/main/java
 │   └── com/calculator/rest
 │       ├── CalculatorConfig.java
 │       ├── CalculatorController.java
 │       └── KafkaMessage.java
 ├── Dockerfile
 └── pom.xml

calculator-core
 ├── src/main/java
 │   └── com/calculator/core
 │       ├── CalculatorConsumer.java
 │       ├── CalculatorCoreApplication.java
 │       ├── CalculatorService.java
 │       └── KafkaMessage.java
 ├── Dockerfile
 └── pom.xml

docker-compose.yml
pom.xml
```

## Prerequisites

- Java 17 or later
- Maven
- Docker & Docker Compose
- Apache Kafka

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd calculator_-main
```
### Building the Project

```bash
mvn clean install
```

### Running the Project

```bash
docker-compose up -d
```

This command will spin up Kafka along with the `calculator-rest` and `calculator-core` services.

### Usage Example
Use `curl` to test the server status:

```bash
curl http://localhost:8080/api/test
```

Response example:

```bash
{"status":"UP"}
```

Use `curl` to perform an arithmetic operation:

```bash
curl http://localhost:8080/api/calculate -H "Content-Type: application/json" -d "{\"operation\":\"sum\",\"a\":2,\"b\":2}"
```

Response example:

```json
{
    "result": 4
}
```
Operation Examples:

```bash
Subtraction = sub
Addition = sum
Multiplication = mul
Division = div
```

## Technologies Used

- **Spring Boot**: REST API and dependency management.
- **Apache Kafka**: Asynchronous communication.
- **Docker Compose**: Container orchestration.

## Contribution

Feel free to submit pull requests and report issues or improvements.

## License

This project is open-source and available under the MIT License.
