package com.example.cartservice.service.impl;

import com.example.cartservice.error.CartNotFound;
import com.example.cartservice.model.dto.CartDTO;
import com.example.cartservice.model.entity.CartEntity;
import com.example.cartservice.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CartServiceImpl cartService;


    @Test
    void createCart_NotExist_ShouldCreateNewCart() {

        Long userId = 1L;
        when(this.cartRepository.existsById(userId)).thenReturn(false);

        this.cartService.createCartIfNotExist(userId);

        verify(this.cartRepository, times(1)).existsById(userId);
        verify(this.cartRepository, times(1)).save(any(CartEntity.class));
    }

    @Test
    void createCart_Exist_ShouldNotCreateNewCart() {

        Long userId = 1L;
        when(this.cartRepository.existsById(userId)).thenReturn(true);

        this.cartService.createCartIfNotExist(userId);

        verify(this.cartRepository, times(1)).existsById(userId);
        verify(this.cartRepository, never()).save(any(CartEntity.class));
    }

    @Test
    void getCartForUser_Exist_ShouldReturnCartDTO() {

        Long userId = 1L;
        CartEntity cartEntity = new CartEntity();
        cartEntity.setUserId(userId);
        cartEntity.setCartItems(new ArrayList<>());
        Optional<CartEntity> optionalCartEntity = Optional.of(cartEntity);
        CartDTO cartDTO = new CartDTO();

        when(this.cartRepository.findByUserId(userId)).thenReturn(optionalCartEntity);
        when(this.modelMapper.map(cartEntity, CartDTO.class)).thenReturn(cartDTO);

        CartDTO result = this.cartService.getCartForUser(userId);

        verify(this.cartRepository, times(1)).findByUserId(userId);
        verify(this.modelMapper, times(1)).map(cartEntity, CartDTO.class);
        assertEquals(cartDTO, result);
    }

    @Test
    void getCartForUser_cartDoesNotExist_shouldThrowCartNotFound() {
        Long userId = 1L;
        when(this.cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        CartNotFound thrown = assertThrows(
                CartNotFound.class,
                () -> this.cartService.getCartForUser(userId),
                "Expected CartNotFound to be thrown"
        );

        verify(this.cartRepository, times(1)).findByUserId(userId);
        verify(this.modelMapper, never()).map(any(), any());
        assertEquals("Cart not found for user with id " + userId, thrown.getMessage());
    }
}
