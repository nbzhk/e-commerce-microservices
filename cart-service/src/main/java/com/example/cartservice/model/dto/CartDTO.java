package com.example.cartservice.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
public class CartDTO {

    private Long userId;
    private List<ProductDataDTO> cartItems;
    private BigDecimal totalPrice;
}
