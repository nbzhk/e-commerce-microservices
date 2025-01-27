package com.example.cartservice.service.impl;

import com.example.cartservice.error.CartNotFound;
import com.example.cartservice.model.dto.CartDTO;
import com.example.cartservice.model.entity.CartEntity;
import com.example.cartservice.repository.CartRepository;
import com.example.cartservice.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    public CartServiceImpl(CartRepository cartRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
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

    @Override
    public CartDTO getCartForUser(Long userId) {
        Optional<CartEntity> userCart = this.cartRepository.findByUserId(userId);

        if (userCart.isPresent()) {
            return this.modelMapper.map(userCart.get(), CartDTO.class);
        }

        throw new CartNotFound("Cart not found for user with id " + userId);
    }
}
