# Product Microservice

A product management microservice built with Spring Boot that handles product creation, updates, retrieval, and deletion as part of the e-commerce microservices architecture.

## Features

- Product creation and management
- Category-based product retrieval
- Product updates and deletion
- Input validation
- JWT-based security integration
- Exception handling for product operations

## Technologies

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT (JSON Web Tokens)
- Model Mapper
- Spring Cloud Netflix Eureka Client
- Lombok
- Maven

## API Endpoints

### Product Controller (`/api/products`)

| Method | Endpoint                    | Description                  | Request Body         | Response           |
|--------|----------------------------|------------------------------|---------------------|-------------------|
| POST   | /create                    | Create new product          | ProductCreationDTO  | ProductDataDTO    |
| GET    | /get/{id}                  | Get product by ID           | -                   | ProductDataDTO    |
| GET    | /allBy/category={category} | Get products by category    | -                   | List<ProductDataDTO> |
| DELETE | /delete/{id}               | Delete product              | -                   | No Content (204)  |
| PUT    | /update/{id}               | Update product details      | ProductUpdateDTO    | ProductDataDTO    |

## Data Transfer Objects (DTOs)

### ProductCreationDTO
```java
{
    "name": "string",
    "description": "string",
    "price": "decimal",
    "category": "string",
    "stockQuantity": "integer"
}
```

### ProductUpdateDTO
```java
{
    "name": "string",
    "description": "string",
    "price": "decimal",
    "stockQuantity": "integer"
}
```

### ProductDataDTO
```java
{
    "id": "long",
    "name": "string",
    "description": "string",
    "price": "decimal",
    "category": "string",
    "stockQuantity": "integer"
}
```


## Security

The service implements several security measures:

- JWT-based authentication
- Input validation using Jakarta Validation
- Exception handling for product operations
- Secure product data handling

## Integration with Other Services

This product service is designed to work within the microservices' architecture:

- Registers with Eureka Server for service discovery
- Integrates with authentication service for JWT validation
- Can be called by other microservices for product information

## Error Handling

The service includes exception handling for:

- ProductNotFoundException
- ProductRegistrationException
- Validation errors
- General service exceptions

## API Response Examples

### Create Product Response
```json
{
    "id": 1,
    "name": "Sample Product",
    "description": "Product description",
    "price": 99.99,
    "category": "Electronics",
    "stockQuantity": 100
}
```

### Get Products by Category Response
```json
[
    {
        "id": 1,
        "name": "Product 1",
        "description": "Description 1",
        "price": 99.99,
        "category": "Electronics",
        "stockQuantity": 100
    },
    {
        "id": 2,
        "name": "Product 2",
        "description": "Description 2",
        "price": 149.99,
        "category": "Electronics",
        "stockQuantity": 50
    }
]
```
