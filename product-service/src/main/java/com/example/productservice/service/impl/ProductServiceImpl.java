package com.example.productservice.service.impl;

import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.exception.ProductRegistrationException;
import com.example.productservice.model.dto.CategoryDTO;
import com.example.productservice.model.dto.ProductCreationDTO;
import com.example.productservice.model.dto.ProductDataDTO;
import com.example.productservice.model.entity.CategoryEntity;
import com.example.productservice.model.entity.ProductEntity;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ProductDataDTO createProduct(ProductCreationDTO productCreationDTO) throws ProductRegistrationException {

        try {
            CategoryEntity category = createCategoryIfNotExist(productCreationDTO.getCategory());

            ProductEntity newProduct = this.modelMapper.map(productCreationDTO, ProductEntity.class);

            newProduct.setCategory(category);
            newProduct.setCreatedAt(LocalDateTime.now());
            newProduct.setUpdatedAt(LocalDateTime.now());

            return this.modelMapper.map(productRepository.save(newProduct), ProductDataDTO.class);
        } catch (Exception e) {
            throw new ProductRegistrationException("Product creation was unsuccessful");
        }

    }

    @Override
    public ProductDataDTO getById(Long id) throws ProductNotFoundException {
        Optional<ProductEntity> optProduct = this.productRepository.findById(id);

        if (optProduct.isPresent()) {
            return this.modelMapper.map(optProduct.get(), ProductDataDTO.class);
        }

        log.error("Product with id {} not found", id);
        throw new ProductNotFoundException("Product with id: " + id + " was not found");
    }

    @Override
    public void deleteProduct(Long id) throws ProductNotFoundException {
        Optional<ProductEntity> product = this.productRepository.findById(id);
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product with id: " + id + " was not found");
        }

        this.productRepository.delete(product.get());
    }

    private CategoryEntity createCategoryIfNotExist(CategoryDTO category) {
        if (!this.categoryRepository.existsByName(category.getName())) {
            CategoryEntity newCategory = new CategoryEntity();
            newCategory.setName(category.getName());
            return this.categoryRepository.save(newCategory);
        }

        return this.categoryRepository.findByName(category.getName());
    }


}
