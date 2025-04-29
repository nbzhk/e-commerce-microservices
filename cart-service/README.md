# Cart Microservice
A Cart Microservice build with Spring Boot that manages user shopping carts.

## Features
- Automatically creates a cart for a user it does not exist
- Retrieves a user's shopping cart
- Build with Spring Cloud for service discovery and communication

## Technologies
- Java 21
- Spring Boot
- Spring Data JPA
- Spring Cloud
- Netflix Eureka (Service Discovery)
- OpenFeign
- MySQL (Production DB)
- H2 (In-memory DB for tests)
- ModelMapper
- Maven
- JUnit 5
- Spring Boot Test

## API Endpoints
Cart Controller (/api/carts)

| Method | Endpoint             | Description                         | Request Body | Response                          |
|--------|----------------------|-------------------------------------|--------------|-----------------------------------|
| POST   | /create/{userId}     | Creates a cart for a user if absent | -            | `204 No Content`                  |
| GET    | /get/{userId}        | Retrieves cart for a user           | -            | `CartDTO` with cart details       |

## Security
This service does not handle authentication directly but is expected to be secured at the API Gateway or via inter-service token validation.

##Integration with Other Services
This cart service is designed to work within the microservice ecosystem:

- Registers with Eureka for service discovery
- Uses OpenFeign for communication with other services (if extended)
- Can be accessed via the API Gateway under the route: /api/carts/**

## Error Handling
Includes custom exception handling for:
- Cart not found for the given user
- Internal persistence-related errors

## Project Structure
- CartServiceImpl handles business logic (create and fetch cart)
- CartController exposes REST endpoints
- CartEntity and CartDTO represent cart data
- CartRepository interacts with the database