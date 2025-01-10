# User Microservice API Documentation

## Overview
The `UserController` provides RESTful endpoints for managing user resources in the system. It allows clients to register users, update user details, retrieve user information, and delete users. The API follows common HTTP conventions and returns appropriate status codes and headers for each operation.

---

## Endpoints

### 1. Register User
**POST** `/api/users/register`

Registers a new user in the system. Username, Email and Password are required.

#### Request:
- **Headers**:
    - `Content-Type`: `application/json`
- **Body(example)**:
  ```json
  {
      "username": "exampleUsername",
      "email": "example@example.com",
      "password": "securePassword123"
  }
  ```

#### Response:
- **Status Code**: `201 Created`
- **Headers**:
    - `Content-Type`: `application/json`
    - `Location`: URL of the newly created user resource, e.g., `/api/users/{id}`
- **Body**:
  ```json
  {
      "username": "exampleUsername",
      "email": "example@example.com"
  }
  ```

---

### 2. Update User Details
**PUT** `/api/users/update/{id}`

Updates the details of an existing user.

#### Request:
- **Path Parameters**:
    - `id`: The ID of the user to be updated.
- **Headers**:
    - `Content-Type`: `application/json`
- **Body**:
  ```json
  {
      "firstName": "Pesho",
      "lastName": "Pesheff",
      "phoneNumber": "1234567890",
      "address": "Street str.",
      "city": "Las Vegas",
      "country": "USA",
      "zip": "12345"
  }
  ```

#### Response:
- **Status Code**: `200 OK`
- **Headers**:
    - `Content-Type`: `application/json`
    - `Cache-Control`: `no-cache, no-store, must-revalidate`
    - `Last-Modified`: Timestamp of the update operation.
- **Body**:
  ```json
   {
      "firstName": "Pesho",
      "lastName": "Pesheff",
      "phoneNumber": "1234567890",
      "address": "Street str.",
      "city": "Las Vegas",
      "country": "USA",
      "zip": "12345"
  }
  ```

---

### 3. Get User Details
**GET** `/api/users/details/{id}`

Retrieves the details of an existing user.

#### Request:
- **Path Parameters**:
    - `id`: The ID of the user to retrieve.

#### Response:
- **Status Code**: `200 OK`
- **Headers**:
    - `Content-Type`: `application/json`
- **Body**:
  ```json
   {
      "firstName": "Pesho",
      "lastName": "Pesheff",
      "phoneNumber": "1234567890",
      "address": "Street str.",
      "city": "Las Vegas",
      "country": "USA",
      "zip": "12345"
  }
  ```

---

### 4. Delete User
**DELETE** `/api/users/delete/{id}`

Deletes an existing user from the system.

#### Request:
- **Path Parameters**:
    - `id`: The ID of the user to delete.

#### Response:
- **Status Code**: `204 No Content`

---

## Headers
### General Headers
- **Content-Type**:
    - For successful requests: `application/json`
    - For errors: `application/problem+json`

### Generated Headers by `HeaderGenerator`
#### Success POST:
- **Location**: Points to the URI of the newly created resource.
- **Content-Type**: `application/json`

#### Success PUT:
- **Location**: Points to the URI of the updated resource.
- **Content-Type**: `application/json`
- **Cache-Control**: `no-cache, no-store, must-revalidate`
- **Last-Modified**: Timestamp indicating the last update.

---

## Error Handling
When an error occurs, the API returns a standard error response with the following format:

```json
{
    "timestamp": "2025-01-08T12:34:56.789+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Specific error message",
    "path": "/api/users/register"
}
```

---


