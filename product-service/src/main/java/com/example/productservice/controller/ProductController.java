package com.example.productservice.controller;

import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.exception.ProductRegistrationException;
import com.example.productservice.model.dto.ProductCreationDTO;
import com.example.productservice.model.dto.ProductDataDTO;
import com.example.productservice.model.dto.ProductUpdateDTO;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:8080")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDataDTO> createProduct(@Valid @RequestBody ProductCreationDTO productCreationDTO) throws ProductRegistrationException {
        ProductDataDTO product = this.productService.createProduct(productCreationDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/products/get/{id}")
                .build(product.getId());

        return ResponseEntity.created(location).body(product);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDataDTO> getProduct(@PathVariable Long id) throws ProductNotFoundException {
        ProductDataDTO product = this.productService.getById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/allBy/category={category}")
    public ResponseEntity<List<ProductDataDTO>> getProductByCategory(@PathVariable String category) {
        List<ProductDataDTO> productsByCategory = this.productService.getProductsByCategory(category);
        return new ResponseEntity<>(productsByCategory, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws ProductNotFoundException {
        this.productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDataDTO> updateProduct(@PathVariable Long id,
                                                        @Valid @RequestBody ProductUpdateDTO productUpdateDTO) throws ProductNotFoundException {
        ProductDataDTO product = this.productService.updateProduct(id, productUpdateDTO);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
