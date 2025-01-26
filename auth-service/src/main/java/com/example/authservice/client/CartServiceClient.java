package com.example.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "cart-service")
public interface CartServiceClient {

    @PostMapping("/api/carts/create/{userId}")
    void createCartIfNotExist(@PathVariable Long userId);
}
