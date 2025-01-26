package com.example.cartservice.controller;

import com.example.cartservice.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<Void> createCartIfNotExist(@PathVariable Long userId) {
        this.cartService.createCartIfNotExist(userId);
        return ResponseEntity.noContent().build();
    }
}
