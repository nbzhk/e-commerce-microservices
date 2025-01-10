package com.example.usermicroservice.model.dto;


import com.example.usermicroservice.validation.annotations.UniqueEmail;
import com.example.usermicroservice.validation.annotations.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO {
    @Size(min = 4, max = 20)
    @NotBlank
    @UniqueUsername
    private String username;
    @Email
    @NotBlank
    @UniqueEmail
    private String email;
    @Size(min = 4, max = 20)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
