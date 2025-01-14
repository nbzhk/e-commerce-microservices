package com.example.productservice.model.entity;

import com.example.productservice.model.dto.ProductUpdateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Double rating;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void updateEntity(ProductUpdateDTO productUpdateDTO) {
        if (productUpdateDTO.getName() != null) {
            this.name = productUpdateDTO.getName();
        }

        if (productUpdateDTO.getDescription() != null) {
            this.description = productUpdateDTO.getDescription();
        }

        if (productUpdateDTO.getPrice() != null) {
            this.price = productUpdateDTO.getPrice();
        }

        if (productUpdateDTO.getStockQuantity() >= 0) {
            this.stockQuantity = productUpdateDTO.getStockQuantity();
        }

        if (productUpdateDTO.getImageUrl() != null) {
            this.imageUrl = productUpdateDTO.getImageUrl();
        }
        if (productUpdateDTO.getRating() != null) {
            this.rating = productUpdateDTO.getRating();
        }
    }

}
