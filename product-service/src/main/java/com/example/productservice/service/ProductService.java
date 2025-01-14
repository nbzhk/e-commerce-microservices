package com.example.productservice.service;

import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.exception.ProductRegistrationException;
import com.example.productservice.model.dto.ProductCreationDTO;
import com.example.productservice.model.dto.ProductDataDTO;

public interface ProductService {

    ProductDataDTO createProduct(ProductCreationDTO productCreationDTO) throws ProductRegistrationException;

    ProductDataDTO getById(Long id) throws ProductNotFoundException;

    void deleteProduct(Long id) throws ProductNotFoundException;
}
