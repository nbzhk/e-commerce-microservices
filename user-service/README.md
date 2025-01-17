# User Microservice

A user management microservice built with Spring Boot that handles user registration, profile management, and user role administration as part of the e-commerce microservices architecture.

## Features

- User registration and management
- User profile updates
- Role-based user administration
- User details retrieval
- Account deletion
- User promotion capabilities
- Integration with Spring Security for password encryption
- Validation for user input

## Technologies

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- H2 Database (for testing)
- Model Mapper
- Spring Cloud Netflix Eureka Client
- Maven

## API Endpoints

### User Controller (`/api/users`)

| Method | Endpoint              | Description                 | Request Body              | Response                    |
|--------|----------------------|-----------------------------|--------------------------|-----------------------------|
| POST   | /register            | Register new user           | UserRegistrationDTO      | UserResponseDTO            |
| PUT    | /update/{id}         | Update user details         | UserDetailsDTO           | UserDetailsDTO             |
| GET    | /details/{id}        | Get user details            | -                        | UserDetailsDTO             |
| DELETE | /delete/{id}         | Delete user account         | -                        | No Content (204)           |
| GET    | /find/{username}     | Find user by username       | -                        | UserLoginDTO               |
| PUT    | /promote/{username}  | Promote user to admin       | -                        | Success message            |

## Data Transfer Objects (DTOs)

### UserRegistrationDTO
```java
{
    "username": "string",
    "password": "string",
    "email": "string",
    "firstName": "string",
    "lastName": "string"
}
```

### UserDetailsDTO
```java
{
    "email": "string",
    "firstName": "string",
    "lastName": "string",
    "address": "string",
    "phoneNumber": "string"
}
```

## Security

The service implements several security measures:

- Password encryption using Spring Security's crypto module
- Input validation using Jakarta Validation
- Role-based access control
- Secure user data handling

## Integration with Other Services

This user service is designed to work within the microservices' architecture:

- Registers with Eureka Server for service discovery
- Can be called by other microservices for user information
- Handles user authentication data for the auth-service


## Error Handling

The service includes exception handling for:
- Invalid user input
- User not found scenarios
- Duplicate username/email
- Unauthorized access attempts
- Validation errors

