# Authentication Microservice

An authentication microservice built with Spring Boot that handles user authentication, JWT token management, and session control as part of the microservices' architecture.

## Features

- User authentication with username and password
- JWT (JSON Web Token) based authentication
- Refresh token support for extended sessions
- Token validation endpoint
- Secure logout mechanism
- Integration with Spring Cloud for microservices architecture

## Technologies

- Java 21
- Spring Boot
- Spring Security
- Spring Cloud
- JSON Web Tokens (JWT)
- JPA/Hibernate
- MySQL
- Maven
- JUnit 5
- Spring Boot Test
- Spring Security Test utilities

## API Endpoints

### Authentication Controller (`/auth`)

| Method | Endpoint    | Description                    | Request Body                      | Response                         |
|--------|------------|--------------------------------|-----------------------------------|----------------------------------|
| POST   | /login     | Authenticate user              | `{ "username": "", "password": ""}` | JWT token + Refresh token        |
| POST   | /validate  | Validate JWT token             | -                                 | Boolean (token validity status)  |
| POST   | /refresh   | Refresh expired JWT token      | `{ "refreshToken": "" }`          | New JWT token                    |
| POST   | /logout    | Logout user                    | `{ "username": "" }`              | Logout confirmation              |


## Security

This service implements several security measures:

- JWT-based authentication
- Refresh token rotation
- Token expiration
- Secure password handling
- CORS configuration
- HTTP Security headers

## Integration with Other Services

This authentication service is designed to work within the microservice architecture:

- Uses Eureka for service discovery
- Implements OpenFeign for service-to-service communication
- Includes Load Balancer for scalability


## Error Handling

The service includes custom exception handling for:
- Token refresh failures
- Invalid credentials
- Expired tokens
- Username not found scenarios
