package com.example.productservice.model.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class ProductUpdateDTO {

    private String name;
    @Length(max = 1000)
    private String description;
    @Positive
    private BigDecimal price;
    private CategoryDTO category;
    @Positive
    private int stockQuantity;
    private String imageUrl;
    @Positive
    private Double rating;

}
