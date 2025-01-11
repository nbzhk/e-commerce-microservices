package com.example.authservice.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginRequestDTO {
    private String username;
    private String password;
    private List<String> roles;
}
