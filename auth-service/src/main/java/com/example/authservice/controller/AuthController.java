package com.example.authservice.controller;

import com.example.authservice.exception.TokenRefreshException;
import com.example.authservice.model.dto.JwtResponseDTO;
import com.example.authservice.model.dto.LoginRequestDTO;
import com.example.authservice.model.dto.RefreshTokenDTO;
import com.example.authservice.model.dto.TokenRequestDTO;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.RefreshTokenService;
import com.example.authservice.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) throws TokenRefreshException {
        String token = authService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        RefreshTokenDTO refreshToken = this.refreshTokenService.createRefreshToken(loginRequestDTO.getUsername());

        return ResponseEntity.ok(new JwtResponseDTO(token, refreshToken.getToken(), "Bearer"));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            boolean isValid = authService.validateToken(token);

            if (isValid) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.ok(false);
            }

        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody TokenRequestDTO request) throws TokenRefreshException {
        try {
            RefreshTokenDTO dto = this.refreshTokenService.findByToken(request.getRefreshToken());

            dto = this.refreshTokenService.verifyExpiration(dto);

            String newResponse = this.jwtUtil.generateToken(dto.getUsername(), "role");

            return ResponseEntity.ok(new JwtResponseDTO(newResponse, dto.getToken(), "Bearer"));

        } catch (RuntimeException e) {
            throw new TokenRefreshException(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LoginRequestDTO loginRequestDTO) {
        boolean deleted = this.refreshTokenService.deleteForUsername(loginRequestDTO.getUsername());

        if (deleted) {
            return ResponseEntity.ok("Logged out successfully");
        }

        throw new UsernameNotFoundException("Logout failed");

    }
}
