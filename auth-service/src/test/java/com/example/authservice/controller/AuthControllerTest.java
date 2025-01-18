package com.example.authservice.controller;

import com.example.authservice.exception.TokenRefreshException;
import com.example.authservice.model.dto.AuthResponseDTO;
import com.example.authservice.model.dto.LoginRequestDTO;
import com.example.authservice.model.dto.RefreshTokenDTO;
import com.example.authservice.model.dto.TokenRequestDTO;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.RefreshTokenService;
import com.example.authservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private LoginRequestDTO loginRequest;
    private RefreshTokenDTO refreshTokenDTO;
    private TokenRequestDTO tokenRequest;
    private AuthResponseDTO authResponseDTO;
    private static final String TEST_TOKEN = "test.jwt.token";
    private static final String TEST_REFRESH_TOKEN = "test-refresh-token";
    private static final List<String> TEST_ROLES = new ArrayList<>(List.of("USER", "ADMIN"));

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequestDTO();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        refreshTokenDTO = new RefreshTokenDTO();
        refreshTokenDTO.setToken(TEST_REFRESH_TOKEN);
        refreshTokenDTO.setUsername("testuser");

        tokenRequest = new TokenRequestDTO();
        tokenRequest.setRefreshToken(TEST_REFRESH_TOKEN);

        authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken(TEST_TOKEN);
        authResponseDTO.setRoles(TEST_ROLES);
    }

    @Test
    void testLogin_ValidCredentials_ReturnsJwtResponse() throws Exception, TokenRefreshException {
        when(authService.login(anyString(), anyString())).thenReturn(authResponseDTO);
        when(refreshTokenService.createRefreshToken(anyString(), any())).thenReturn(refreshTokenDTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer " + TEST_TOKEN))
                .andExpect(jsonPath("$.accessToken").value(TEST_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(TEST_REFRESH_TOKEN))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    void testLogin_InvalidCredentials_ReturnsBadRequest() throws Exception {
        when(authService.login(anyString(), anyString()))
                .thenThrow(new UsernameNotFoundException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testValidateToken_ValidToken_ReturnsOk() throws Exception {
        when(authService.validateToken(TEST_TOKEN)).thenReturn(true);

        mockMvc.perform(post("/auth/validate")
                        .header("Authorization", "Bearer " + TEST_TOKEN))
                .andExpect(status().isOk())
                .andExpect(header().string("Pragma", "no-cache"))
                .andExpect(header().string("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testValidateToken_InvalidToken_ReturnsUnauthorized() throws Exception {
        when(authService.validateToken(TEST_TOKEN)).thenReturn(false);

        mockMvc.perform(post("/auth/validate")
                        .header("Authorization", "Bearer " + TEST_TOKEN))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", "Bearer error=\"invalid-token\""));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testRefreshToken_ValidToken_ReturnsNewToken() throws Exception, TokenRefreshException {
        when(refreshTokenService.findByToken(TEST_REFRESH_TOKEN)).thenReturn(refreshTokenDTO);
        when(refreshTokenService.verifyExpiration(any(RefreshTokenDTO.class))).thenReturn(refreshTokenDTO);
        when(jwtUtil.generateToken(anyString(), any())).thenReturn(TEST_TOKEN);
        when(jwtUtil.extractRoles(anyString())).thenReturn(new ArrayList<>(List.of("ROLE_USER")));

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer " + TEST_TOKEN))
                .andExpect(jsonPath("$.accessToken").value(TEST_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(TEST_REFRESH_TOKEN))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testRefreshToken_ExpiredToken_ReturnsUnauthorized() throws Exception, TokenRefreshException {
        when(refreshTokenService.findByToken(TEST_REFRESH_TOKEN)).thenReturn(refreshTokenDTO);
        when(refreshTokenService.verifyExpiration(any(RefreshTokenDTO.class)))
                .thenThrow(new TokenRefreshException("Refresh token was expired"));

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testLogout_ValidUser_ReturnsOk() throws Exception {
        when(refreshTokenService.deleteForUsername(anyString())).thenReturn(true);

        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", "refresh-token=; Path=/; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; HttpOnly"))
                .andExpect(content().string("Logged out successfully"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testLogout_InvalidUser_ReturnsNotFound() throws Exception {
        when(refreshTokenService.deleteForUsername(anyString())).thenReturn(false);

        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound());
    }
}

