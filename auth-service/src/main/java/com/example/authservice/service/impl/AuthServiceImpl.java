package com.example.authservice.service.impl;

import com.example.authservice.client.UserServiceClient;
import com.example.authservice.exception.InvalidLoginException;
import com.example.authservice.model.dto.AuthResponseDTO;
import com.example.authservice.model.dto.LoginRequestDTO;
import com.example.authservice.service.AuthService;
import com.example.authservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserServiceClient userServiceClient;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserServiceClient userServiceClient, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userServiceClient = userServiceClient;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponseDTO login(String username, String password) {
        LoginRequestDTO user = userServiceClient.findByUsername(username).getBody();

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidLoginException("Invalid username or password");
        }

        String token = this.jwtUtil.generateToken(username, user.getRoles());

        return new AuthResponseDTO(token, user.getRoles());
    }

    @Override
    public boolean validateToken(String token) {

        String username = jwtUtil.extractUsername(token);
        return jwtUtil.isTokenValid(token, username);

    }
}
