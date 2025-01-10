package com.example.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuthServiceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceDemoApplication.class, args);
    }

}
