package com.example.authservice.service;

import com.example.authservice.model.dto.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO login(String username, String password);

    boolean validateToken(String token);
}
