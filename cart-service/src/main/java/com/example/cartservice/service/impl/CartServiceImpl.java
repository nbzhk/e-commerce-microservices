package com.example.cartservice.service.impl;

import com.example.cartservice.model.entity.CartEntity;
import com.example.cartservice.repository.CartRepository;
import com.example.cartservice.service.CartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public void createCartIfNotExist(Long userId) {
        if (!this.cartRepository.existsById(userId)) {
            CartEntity cartEntity = new CartEntity();
            cartEntity.setUserId(userId);
            cartEntity.setCartItems(new ArrayList<>());

            this.cartRepository.save(cartEntity);
        }
    }
}
