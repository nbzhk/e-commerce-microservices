package com.example.authservice.service;

import com.example.authservice.exception.TokenRefreshException;
import com.example.authservice.model.dto.RefreshTokenDTO;

public interface RefreshTokenService {
    RefreshTokenDTO createRefreshToken(String username) throws TokenRefreshException;

    RefreshTokenDTO findByToken(String token);

    RefreshTokenDTO verifyExpiration(RefreshTokenDTO refreshToken) throws TokenRefreshException;

    boolean deleteForUsername(String username);
}
