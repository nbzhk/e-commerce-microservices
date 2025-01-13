package com.example.productservice.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class ProductCreationDTO {

    @NotBlank
    private String name;
    @NotBlank
    @Length(max = 1000)
    private String description;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotNull
    private CategoryDTO category;
    @NotNull
    @Positive
    private int stockQuantity;
    @NotBlank
    private String imageUrl;
    @Positive
    @Min(0)
    @Max(5)
    private Double rating;
}
