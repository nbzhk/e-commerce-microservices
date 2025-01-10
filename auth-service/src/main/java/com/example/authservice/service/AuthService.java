package com.example.authservice.service;

public interface AuthService {
    String login(String username, String password);

    boolean validateToken(String token);
}
