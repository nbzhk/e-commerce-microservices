package com.example.authservice.service.impl;

import com.example.authservice.exception.TokenRefreshException;
import com.example.authservice.model.dto.RefreshTokenDTO;
import com.example.authservice.model.entity.RefreshTokenEntity;
import com.example.authservice.repository.RefreshTokenRepository;
import com.example.authservice.service.RefreshTokenService;
import com.example.authservice.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh.token.expiration}")
    private long refreshTokenExpiration;


    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil, ModelMapper modelMapper) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }


    @Override
    public RefreshTokenDTO createRefreshToken(String username, List<String> roles) throws TokenRefreshException {

        this.refreshTokenRepository.deleteByUsername(username);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUsername(username);
        refreshTokenEntity.setRoles(roles);
        refreshTokenEntity.setExpires(Instant.now().plusSeconds(refreshTokenExpiration));

        String token = this.jwtUtil.generateRefreshToken(username, roles);
        refreshTokenEntity.setToken(token);

        try {
            this.refreshTokenRepository.save(refreshTokenEntity);
        } catch (DataIntegrityViolationException e) {
            throw new TokenRefreshException("Could not create refresh token");
        }

        return this.modelMapper.map(refreshTokenEntity, RefreshTokenDTO.class);
    }

    @Override
    public RefreshTokenDTO findByToken(String token) {
        Optional<RefreshTokenEntity> optToken = this.refreshTokenRepository.findByToken(token);

        if (optToken.isEmpty()) {
            throw new RuntimeException("Token not found");
        }

        return this.modelMapper.map(optToken.get(), RefreshTokenDTO.class);
    }

    @Override
    public RefreshTokenDTO verifyExpiration(RefreshTokenDTO refreshToken) throws TokenRefreshException {
        if (refreshToken.getExpires().compareTo(Instant.now()) < 0) {
            Optional<RefreshTokenEntity> token =
                    this.refreshTokenRepository.findByToken(refreshToken.getToken());
            token.ifPresent(this.refreshTokenRepository::delete);

            throw new TokenRefreshException("Refresh token was expired");
        }

        return refreshToken;
    }

    @Override
    public boolean deleteForUsername(String username) {
        return this.refreshTokenRepository.deleteByUsername(username) > 0;
    }

}
