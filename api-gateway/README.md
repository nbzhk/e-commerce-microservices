# Api Gateway

An API Gateway (Spring Cloud Gateway) build with Spring Boot that handles routing for the e-commerce microservices architecture. 

## Features
- Dynamic routing to microservices
- Centralized entry point for all client requests
- Integration with Eureka for service discovery

## Technologies
- Java 21
- Spring Boot
- Spring Cloud Gateway
- Spring Cloud Eureka Client
- Maven

## API Endpoint
The API Gateway dynamically routes to the following services:

| Service         | Path Prefix(es)     | Description                              |
|-----------------|---------------------|------------------------------------------|
| Auth Service    | `/auth/**`          | Routes requests to the Authentication Service. |
| User Service    | `/api/users/**`     | Routes requests to the User Service.     |
| Product Service | `/api/products/**`  | Routes requests to the Product Service.  |
| Cart Service    | `/api/carts/**`     | Routes requests to the Cart Service.     |


## Microservice Communication
The API Gateway uses Spring Cloud Gateway to route incoming requests to the appropriate microservices.  It relies on Eureka for service discovery, allowing it to dynamically locate service instances.