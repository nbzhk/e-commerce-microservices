package com.example.authservice.service;

import com.example.authservice.exception.TokenRefreshException;
import com.example.authservice.model.dto.RefreshTokenDTO;

import java.util.List;

public interface RefreshTokenService {
    RefreshTokenDTO createRefreshToken(String username, List<String> roles) throws TokenRefreshException;

    RefreshTokenDTO findByToken(String token);

    RefreshTokenDTO verifyExpiration(RefreshTokenDTO refreshToken) throws TokenRefreshException;

    boolean deleteForUsername(String username);
}
