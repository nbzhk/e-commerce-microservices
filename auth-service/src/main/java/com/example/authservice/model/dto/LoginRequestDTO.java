package com.example.authservice.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class LoginRequestDTO {
    private String username;
    private String password;
    private List<String> roles;
}
