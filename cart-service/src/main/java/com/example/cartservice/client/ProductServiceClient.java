package com.example.cartservice.client;

import com.example.cartservice.model.dto.ProductDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @GetMapping("/api/products/get/{id}")
    ProductDataDTO getProduct(@PathVariable("id") Long id);
}
