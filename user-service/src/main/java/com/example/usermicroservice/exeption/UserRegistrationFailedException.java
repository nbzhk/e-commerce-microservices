package com.example.usermicroservice.exeption;

public class UserRegistrationFailedException extends RuntimeException {
    public UserRegistrationFailedException(String message) {
        super(message);
    }
}
