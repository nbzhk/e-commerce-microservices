package com.example.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.RouteLocator;


@SpringBootApplication
public class ApiGatewayApplication {

    @Value("${api.gateway.auth.uri}")
    private String authServerUrl;

    @Value("${api.gateway.user.uri}")
    private String userServiceUrl;

    @Value("${api.gateway.product.uri}")
    private String productServiceUrl;

    @Value("${api.gateway.cart.uri}")
    private String cartServiceUrl;

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri(authServerUrl))
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .uri(userServiceUrl))
                .route("product-service", r -> r
                        .path("/api/products/**")
                        .uri(productServiceUrl))
                .route("cart-service", r -> r
                        .path("/api/carts/**")
                        .uri(cartServiceUrl))
                .build();
    }

}
