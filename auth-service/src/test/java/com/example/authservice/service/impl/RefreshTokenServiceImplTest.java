package com.example.authservice.service.impl;

import com.example.authservice.exception.TokenRefreshException;
import com.example.authservice.model.dto.RefreshTokenDTO;
import com.example.authservice.model.entity.RefreshTokenEntity;
import com.example.authservice.repository.RefreshTokenRepository;
import com.example.authservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTest {

    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_TOKEN = UUID.randomUUID().toString();
    private static final List<String> TEST_ROLES = Arrays.asList("USER", "ADMIN");
    private static final Long REFRESH_TOKEN_EXPIRATION = 86400000L;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private RefreshTokenEntity testEntity;
    private RefreshTokenDTO testDTO;

    @BeforeEach
    public void setUp() {
        testEntity = new RefreshTokenEntity();
        testEntity.setUsername(TEST_USERNAME);
        testEntity.setToken(TEST_TOKEN);
        testEntity.setExpires(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION));

        testDTO = new RefreshTokenDTO();
        testDTO.setToken(TEST_TOKEN);
        testDTO.setUsername(TEST_USERNAME);
        testDTO.setExpires(testEntity.getExpires());

    }

    @Test
    void testCreateRefreshToken_Success() throws TokenRefreshException {
        when(this.refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenReturn(testEntity);

        when(this.modelMapper.map(any(RefreshTokenEntity.class), eq(RefreshTokenDTO.class))).thenReturn(testDTO);

        when(this.jwtUtil.generateRefreshToken(TEST_USERNAME, TEST_ROLES)).thenReturn(TEST_TOKEN);

        RefreshTokenDTO result = this.refreshTokenService.createRefreshToken(TEST_USERNAME, TEST_ROLES);

        assertNotNull(result);
        assertEquals(TEST_USERNAME, result.getUsername());
        verify(this.refreshTokenRepository, times(1)).deleteByUsername(TEST_USERNAME);
        verify(this.refreshTokenRepository, times(1)).save(any(RefreshTokenEntity.class));
    }

    @Test
    void testCreateRefreshToken_Failure_ShouldThrow() {
        when(this.refreshTokenRepository.save(any(RefreshTokenEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Save failed"));

        assertThrows(TokenRefreshException.class,
                () -> this.refreshTokenService.createRefreshToken(TEST_USERNAME, TEST_ROLES));
        verify(this.refreshTokenRepository, times(1)).deleteByUsername(TEST_USERNAME);
    }

    @Test
    void testFindByToken_Success_ShouldReturnRefreshToken() {
        when(this.refreshTokenRepository.findByToken(TEST_TOKEN)).thenReturn(Optional.of(testEntity));

        when(this.modelMapper.map(any(RefreshTokenEntity.class), eq(RefreshTokenDTO.class))).thenReturn(testDTO);

        RefreshTokenDTO result = this.refreshTokenService.findByToken(TEST_TOKEN);

        assertNotNull(result);
        assertEquals(TEST_TOKEN, result.getToken());
        assertEquals(TEST_USERNAME, result.getUsername());
    }

    @Test
    void testFindByToken_Failure_ShouldThrow() {
        when(this.refreshTokenRepository.findByToken(TEST_TOKEN)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> this.refreshTokenService.findByToken(TEST_TOKEN));
    }

    @Test
    void testVerifyExpiration_Success_WithValidToken() throws TokenRefreshException {
        RefreshTokenDTO result = this.refreshTokenService.verifyExpiration(testDTO);
        assertNotNull(result);
        assertEquals(TEST_TOKEN, result.getToken());
    }

    @Test
    void testVerifyExpiration_Failure_WithExpiredToken_ShouldThrow() {
        RefreshTokenDTO expiredDTO = new RefreshTokenDTO();
        expiredDTO.setToken(TEST_TOKEN);
        expiredDTO.setUsername(TEST_USERNAME);
        expiredDTO.setExpires(Instant.now().minusMillis(REFRESH_TOKEN_EXPIRATION));

        when(this.refreshTokenRepository.findByToken(TEST_TOKEN)).thenReturn(Optional.of(testEntity));

        assertThrows(TokenRefreshException.class,
                () -> this.refreshTokenService.verifyExpiration(expiredDTO));
        verify(refreshTokenRepository, times(1)).findByToken(TEST_TOKEN);

    }

    @Test
    void testDeleteForUsername_True_WhenDeleted() {
        when(this.refreshTokenRepository.deleteByUsername(anyString())).thenReturn(1);
        boolean result = this.refreshTokenService.deleteForUsername(TEST_USERNAME);
        assertTrue(result);
    }

    @Test
    void testDeleteForUsername_False_WhenNoTokenFound() {
        when(this.refreshTokenRepository.deleteByUsername(anyString())).thenReturn(0);
        boolean result = this.refreshTokenService.deleteForUsername(TEST_USERNAME);
        assertFalse(result);
    }

}
