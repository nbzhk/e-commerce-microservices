package com.example.authservice.client;

import com.example.authservice.model.dto.LoginRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/find/{username}")
    ResponseEntity<LoginRequestDTO> findByUsername(@PathVariable String username);
}
