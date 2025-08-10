package com.example.cartservice.controller;

import com.example.cartservice.model.dto.CartDTO;
import com.example.cartservice.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/carts")
@CrossOrigin(origins = "http://localhost:8080")
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

    @GetMapping("/get/{userId}")
    public ResponseEntity<CartDTO> getCartForUserId(@PathVariable Long userId) {
        CartDTO cartForUser = this.cartService.getCartForUser(userId);

        return ResponseEntity.ok(cartForUser);
    }
}
