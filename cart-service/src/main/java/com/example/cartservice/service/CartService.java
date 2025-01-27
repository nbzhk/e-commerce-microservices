package com.example.cartservice.service;

import com.example.cartservice.model.dto.CartDTO;

public interface CartService {

    void createCartIfNotExist(Long userId);

    CartDTO getCartForUser(Long userId);

}
