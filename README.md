# 🛒 E-Commerce Microservices System

A modular e-commerce backend application built with Spring Boot and Spring Cloud, following microservices architecture principles. Each service is independently deployable and communicates via REST, managed through Eureka service discovery.

## 📦 Modules

| Service           | Description                             |
|-------------------|-----------------------------------------|
| `eureka-server`   | Service registry (Eureka)               |
| `api-gateway`     | Entry point that routes client requests |
| `auth-service`    | Handles user authentication and JWT     |
| `user-service`    | Manages user accounts and profile data  |
| `product-service` | Handles product catalog and details     |
| `cart-service`    | Manages user shopping carts             |

Each module has its own `README.md` with setup instructions and API documentation.

---

## ⚙️ Tech Stack

- **Java 21**
- **Spring Boot 3.4.1**
- **Spring Cloud 2024.0.0**
- **Maven** (Multi-module build)
- **MySQL** or **H2** (per service)
- **JWT** (Authentication)
- **Eureka** (Service Discovery)
- **OpenFeign** (Inter-service Communication)
- **Spring Cloud Gateway** (API Gateway)
- **Docker** (Containerized deployment)
- **GitHub Actions** (CI/CD pipeline for Docker image builds)

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 21
- Maven
- MySQL (for services with persistence)
- Docker (optional for containerized setup)

---

### 🧪 Run Locally

#### 1. Start `eureka-server`:

```bash
cd eureka-server
mvn spring-boot:run
```

#### 2. Start `api-gateway`
```bash
cd ../api-gateway
mvn spring-boot:run
```

#### 3. Start each microservice in separate terminals:
```bash
cd ../auth-service && mvn spring-boot:run
cd ../user-service && mvn spring-boot:run
cd ../product-service && mvn spring-boot:run
cd ../cart-service && mvn spring-boot:run
```

#### 4. Open the Eureka Dashboard:
- 📍 http://localhost:8761

## OR Run with Docker 🐳
You can spin up the entire system using Docker Compose. All services are pre-built and published as Docker images via GitHub Actions CI/CD.

### 🔧 Build & Run
#### 1.Ensure you have a .env file with the following variables:
```env
#Database common variables
DB_USERNAME={your_db_username}
DB_PASSWORD={your_db_password}

#API Gateway
API_GATEWAY_PORT=8080

#USER SERVICE
USER_SERVICE_PORT=8081
USER_DB_URL=mysql-db:3306/user_service

#AUTH_SERVICE
AUTH_SERVICE_PORT=8082
AUTH_DB_URL=mysql-db:3306/auth_service
JWT_SECRET=8db468cd48154aaf7b7b1ed114fe3cfee1b90635f79b8a5550748ccd85004c61

#PRODUCT SERVICE
PRODUCT_SERVICE_PORT=8083
PRODUCT_DB_URL=mysql-db:3306/product_service

#CART SERVICE
CART_SERVICE_PORT=8084
CART_DB_URL=mysql-db:3306/cart_service
```

#### 2. Run the application 
```bash
docker-compose up --build
```

All services share the e-com-network bridge for internal communication.

## API Routes

| **Endpoint**          | **Routed To**      |
|------------------------|--------------------|
| `/auth/**`             | Auth Service       |
| `/api/users/**`        | User Service       |
| `/api/products/**`     | Product Service    |
| `/api/carts/**`        | Cart Service       |


