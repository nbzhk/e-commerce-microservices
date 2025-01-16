package com.example.authservice.controller;

import com.example.authservice.exception.TokenRefreshException;
import com.example.authservice.model.dto.JwtResponseDTO;
import com.example.authservice.model.dto.LoginRequestDTO;
import com.example.authservice.model.dto.RefreshTokenDTO;
import com.example.authservice.model.dto.TokenRequestDTO;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.RefreshTokenService;
import com.example.authservice.util.JwtUtil;
import org.springframework.http.HttpStatus;
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

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(new JwtResponseDTO(token, refreshToken.getToken(), "Bearer"));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        boolean isValid = authService.validateToken(token);

        if (isValid) {
            return ResponseEntity.ok()
                    .header("Pragma", "no-cache")
                    .header("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0")
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("WWW-Authenticate", "Bearer error=\"invalid-token\"")
                    .build();
        }

    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody TokenRequestDTO request) throws TokenRefreshException {

        RefreshTokenDTO dto = this.refreshTokenService.findByToken(request.getRefreshToken());

        dto = this.refreshTokenService.verifyExpiration(dto);

        String newResponse = this.jwtUtil.generateToken(dto.getUsername(), this.jwtUtil.extractRoles(dto.getToken()));

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newResponse)
                .body(new JwtResponseDTO(newResponse, dto.getToken(), "Bearer"));


    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LoginRequestDTO loginRequestDTO) {
        boolean deleted = this.refreshTokenService.deleteForUsername(loginRequestDTO.getUsername());

        if (deleted) {
            return ResponseEntity.ok()
                    .header("Set-Cookie", "refresh-token=; HttpOnly; Path=/; Max-Age=0")
                    .body("Logged out successfully");
        }

        throw new UsernameNotFoundException("Logout failed");

    }
}
