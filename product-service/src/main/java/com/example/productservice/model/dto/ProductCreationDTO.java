package com.example.productservice.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class ProductCreationDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private CategoryDTO category;
    private int stockQuantity;
    private String imageUrl;
    private Double rating;
}
