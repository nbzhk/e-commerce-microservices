package com.example.productservice.service.impl;

import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.exception.ProductRegistrationException;
import com.example.productservice.model.dto.CategoryDTO;
import com.example.productservice.model.dto.ProductCreationDTO;
import com.example.productservice.model.dto.ProductDataDTO;
import com.example.productservice.model.dto.ProductUpdateDTO;
import com.example.productservice.model.entity.CategoryEntity;
import com.example.productservice.model.entity.ProductEntity;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductEntity productEntity;
    private ProductDataDTO productDataDTO;
    private final Long productId = 1L;

    @BeforeEach
    void setUp() {
        productEntity = new ProductEntity();
        productEntity.setId(productId);
        productEntity.setName("Test Product");
        productEntity.setDescription("Test Description");

        productDataDTO = new ProductDataDTO();
        productDataDTO.setId(productId);
        productDataDTO.setName("Test Product");
        productDataDTO.setDescription("Test Description");
    }

    @Test
    void getById_productExists_shouldReturnProductDataDTO() throws ProductNotFoundException {
        when(this.productRepository.findById(this.productId)).thenReturn(Optional.of(this.productEntity));
        when(this.modelMapper.map(this.productEntity, ProductDataDTO.class)).thenReturn(this.productDataDTO);

        ProductDataDTO result = this.productService.getById(this.productId);

        assertEquals(this.productDataDTO, result);
        verify(this.productRepository, times(1)).findById(this.productId);
        verify(this.modelMapper, times(1)).map(this.productEntity, ProductDataDTO.class);
    }

    @Test
    void getById_productDoesNotExist_shouldThrowProductNotFoundException() {
        when(this.productRepository.findById(this.productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> this.productService.getById(this.productId));
        verify(this.productRepository, times(1)).findById(this.productId);
        verify(this.modelMapper, never()).map(any(), any());
    }

    @Test
    void createProduct_successfulCreation_shouldReturnProductDataDTO() throws ProductRegistrationException {
        ProductCreationDTO creationDTO = new ProductCreationDTO();
        creationDTO.setName("Test Product");
        creationDTO.setDescription("Test Description");
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Test Category");
        creationDTO.setCategory(categoryDTO);

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("Test Category");

        ProductEntity savedProductEntity = new ProductEntity();
        savedProductEntity.setId(this.productId);
        savedProductEntity.setName("Test Product");
        savedProductEntity.setDescription("Test Description");
        savedProductEntity.setCategory(categoryEntity);
        savedProductEntity.setCreatedAt(LocalDateTime.now());
        savedProductEntity.setUpdatedAt(LocalDateTime.now());

        when(this.categoryRepository.existsByName(categoryDTO.getName())).thenReturn(false);
        when(this.categoryRepository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);
        when(this.modelMapper.map(creationDTO, ProductEntity.class)).thenReturn(this.productEntity);
        when(this.productRepository.save(any(ProductEntity.class))).thenReturn(savedProductEntity);
        when(this.modelMapper.map(savedProductEntity, ProductDataDTO.class)).thenReturn(this.productDataDTO);

        ProductDataDTO result = this.productService.createProduct(creationDTO);

        assertEquals(this.productDataDTO, result);
        verify(this.categoryRepository, times(1)).existsByName(categoryDTO.getName());
        verify(this.categoryRepository, times(1)).save(any(CategoryEntity.class));
        verify(this.productRepository, times(1)).save(any(ProductEntity.class));
        verify(this.modelMapper, times(1)).map(creationDTO, ProductEntity.class);
        verify(this.modelMapper, times(1)).map(savedProductEntity, ProductDataDTO.class);
    }

    @Test
    void createProduct_exceptionThrown_shouldThrowProductRegistrationException() {
        ProductCreationDTO creationDTO = new ProductCreationDTO();
        creationDTO.setName("Test Product");
        creationDTO.setDescription("Test Description");
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Test Category");
        creationDTO.setCategory(categoryDTO);

        when(this.categoryRepository.existsByName(categoryDTO.getName())).thenReturn(false);
        when(this.categoryRepository.save(any(CategoryEntity.class))).thenThrow(new RuntimeException("Simulated error"));

        assertThrows(ProductRegistrationException.class, () -> this.productService.createProduct(creationDTO));
        verify(this.categoryRepository, times(1)).existsByName(categoryDTO.getName());
        verify(this.categoryRepository, times(1)).save(any(CategoryEntity.class));
        verify(this.productRepository, never()).save(any(ProductEntity.class));
        verify(this.modelMapper, never()).map(any(), any());
    }

    @Test
    void deleteProduct_productExists_shouldDeleteProduct() throws ProductNotFoundException {
        when(this.productRepository.findById(this.productId)).thenReturn(Optional.of(this.productEntity));

        this.productService.deleteProduct(this.productId);

        verify(this.productRepository, times(1)).findById(this.productId);
        verify(this.productRepository, times(1)).delete(this.productEntity);
    }

    @Test
    void deleteProduct_productDoesNotExist_shouldThrowProductNotFoundException() {
        when(this.productRepository.findById(this.productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> this.productService.deleteProduct(this.productId));
        verify(this.productRepository, times(1)).findById(this.productId);
        verify(this.productRepository, never()).delete(any());
    }

    @Test
    void updateProduct_productExists_shouldReturnUpdatedProductDataDTO() throws ProductNotFoundException {
        // Arrange
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setName("Updated Product");
        updateDTO.setDescription("Updated Description");
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Updated Category");
        updateDTO.setCategory(categoryDTO);

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("Updated Category");

        ProductEntity updatedProductEntity = new ProductEntity();
        updatedProductEntity.setId(this.productId);
        updatedProductEntity.setName("Updated Product");
        updatedProductEntity.setDescription("Updated Description");
        updatedProductEntity.setCategory(categoryEntity);
        updatedProductEntity.setCreatedAt(LocalDateTime.now());
        updatedProductEntity.setUpdatedAt(LocalDateTime.now());

        when(this.productRepository.findById(this.productId)).thenReturn(Optional.of(this.productEntity));
        when(this.categoryRepository.existsByName(categoryDTO.getName())).thenReturn(false);
        when(this.categoryRepository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);
        when(this.productRepository.save(any(ProductEntity.class))).thenReturn(updatedProductEntity);
        when(this.modelMapper.map(updatedProductEntity, ProductDataDTO.class)).thenReturn(this.productDataDTO);

        ProductDataDTO result = this.productService.updateProduct(this.productId, updateDTO);

        assertEquals(this.productDataDTO, result);
        verify(this.productRepository, times(1)).findById(this.productId);
        verify(this.categoryRepository, times(1)).existsByName(categoryDTO.getName());
        verify(this.categoryRepository, times(1)).save(any(CategoryEntity.class));
        verify(this.productRepository, times(1)).save(any(ProductEntity.class));
        verify(this.modelMapper, times(1)).map(updatedProductEntity, ProductDataDTO.class);
    }

    @Test
    void updateProduct_productDoesNotExist_shouldThrowProductNotFoundException() {
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        when(this.productRepository.findById(this.productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> this.productService.updateProduct(this.productId, updateDTO));
        verify(this.productRepository, times(1)).findById(this.productId);
        verify(this.categoryRepository, never()).existsByName(anyString());
        verify(this.categoryRepository, never()).save(any(CategoryEntity.class));
        verify(this.productRepository, never()).save(any(ProductEntity.class));
        verify(this.modelMapper, never()).map(any(), any());
    }

    @Test
    void getProductsByCategory_productsExist_shouldReturnListOfProductDataDTOs() {
        String categoryName = "Test Category";
        List<ProductEntity> productEntityList = List.of(this.productEntity);
        List<ProductDataDTO> productDataDTOList = List.of(this.productDataDTO);

        when(this.productRepository.getProductEntitiesByCategoryName(categoryName)).thenReturn(productEntityList);
        when(this.modelMapper.map(this.productEntity, ProductDataDTO.class)).thenReturn(this.productDataDTO);

        List<ProductDataDTO> result = this.productService.getProductsByCategory(categoryName);

        assertEquals(productDataDTOList, result);
        verify(this.productRepository, times(1)).getProductEntitiesByCategoryName(categoryName);
        verify(this.modelMapper, times(1)).map(this.productEntity, ProductDataDTO.class);
    }

    @Test
    void getProductsByCategory_productsDoNotExist_shouldReturnEmptyList() {
        String categoryName = "NonExisting Category";
        when(this.productRepository.getProductEntitiesByCategoryName(categoryName)).thenReturn(List.of());

        List<ProductDataDTO> result = this.productService.getProductsByCategory(categoryName);

        assertTrue(result.isEmpty());
        verify(this.productRepository, times(1)).getProductEntitiesByCategoryName(categoryName);
        verify(this.modelMapper, never()).map(any(), any());
    }
}
