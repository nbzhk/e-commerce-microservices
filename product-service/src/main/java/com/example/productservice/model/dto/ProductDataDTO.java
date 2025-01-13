package com.example.productservice.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ProductDataDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private CategoryDTO category;
    private int stockQuantity;
    private String imageUrl;
    private Double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
