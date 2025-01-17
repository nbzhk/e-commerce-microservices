package com.example.authservice.service.impl;

import com.example.authservice.client.UserServiceClient;
import com.example.authservice.exception.InvalidLoginException;
import com.example.authservice.model.dto.LoginRequestDTO;
import com.example.authservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private static final String TEST_TOKEN = "testToken";
    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";
    private static final List<String> TEST_ROLES = new ArrayList<>(List.of("USER"));

    @Mock
    private UserServiceClient client;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequestDTO validUserTest;

    @BeforeEach
    void setUp() {
        validUserTest = new LoginRequestDTO();
        validUserTest.setUsername(TEST_USERNAME);
        validUserTest.setPassword(TEST_PASSWORD);
        validUserTest.setRoles(TEST_ROLES);
    }


    @Test
    void testLogin_WithValidCredentials_ShouldReturnToken() {

        ResponseEntity<LoginRequestDTO> response = ResponseEntity.ok(validUserTest);

        when(this.client.findByUsername(anyString())).thenReturn(response);

        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        when(this.jwtUtil.generateToken(validUserTest.getUsername(), validUserTest.getRoles()))
                .thenReturn(TEST_TOKEN);

        String token = this.authService.login(validUserTest.getUsername(), validUserTest.getPassword());

        assertEquals(TEST_TOKEN, token);
    }

    @Test
    void testLogin_WithInvalidCredentials_ShouldThrow() {

        when(this.client.findByUsername(anyString())).thenReturn(ResponseEntity.ok(null));

        assertThrows(InvalidLoginException.class,
                () -> this.authService.login(TEST_USERNAME, TEST_PASSWORD),
                "Invalid username or password");

    }

    @Test
    void testLogin_WithFalsePassword_ShouldThrow() {

        when(this.client.findByUsername(TEST_USERNAME)).thenReturn(ResponseEntity.ok(validUserTest));

        when(this.passwordEncoder.matches(TEST_PASSWORD, validUserTest.getPassword())).thenReturn(false);

        assertThrows(InvalidLoginException.class,
                () -> this.authService.login(TEST_USERNAME, TEST_PASSWORD),
                "Invalid username or password");
    }

    @Test
    void testValidateToken_WithValidToken_ShouldReturnTrue() {
        when(this.jwtUtil.extractUsername(TEST_TOKEN)).thenReturn(TEST_USERNAME);
        when(this.jwtUtil.isTokenValid(TEST_TOKEN, TEST_USERNAME)).thenReturn(true);

        boolean result = this.authService.validateToken(TEST_TOKEN);
        assertTrue(result);
    }

    @Test
    void testValidateToken_WithInvalidToken_ShouldThrow() {
        when(this.jwtUtil.extractUsername(TEST_TOKEN)).thenReturn(TEST_USERNAME);
        when(this.jwtUtil.isTokenValid(TEST_TOKEN, TEST_USERNAME)).thenReturn(false);

        boolean result = this.authService.validateToken(TEST_TOKEN);
        assertFalse(result);
    }



}
