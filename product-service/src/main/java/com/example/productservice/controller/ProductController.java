package com.example.productservice.controller;

import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.exception.ProductRegistrationException;
import com.example.productservice.model.dto.ProductCreationDTO;
import com.example.productservice.model.dto.ProductDataDTO;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDataDTO> createProduct(@Valid @RequestBody ProductCreationDTO productCreationDTO) throws ProductRegistrationException {
        ProductDataDTO product = this.productService.createProduct(productCreationDTO);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDataDTO> getProduct(@PathVariable Long id) throws ProductNotFoundException {
        ProductDataDTO product = this.productService.getById(id);
        return new ResponseEntity<>(product, HttpStatus.FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws ProductNotFoundException {
        this.productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
