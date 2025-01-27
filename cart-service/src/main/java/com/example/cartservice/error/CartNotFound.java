package com.example.cartservice.error;

public class CartNotFound extends RuntimeException {
    public CartNotFound(String message) {
        super(message);
    }
}
