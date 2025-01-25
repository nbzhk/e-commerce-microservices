package com.example.cartservice.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class ProductDataDTO {

    private Long id;
    private int stockQuantity;
    private BigDecimal price;
}
