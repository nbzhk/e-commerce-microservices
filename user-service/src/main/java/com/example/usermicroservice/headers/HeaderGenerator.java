package com.example.usermicroservice.headers;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;


@Service
public class HeaderGenerator {

    private static final Logger log = LoggerFactory.getLogger(HeaderGenerator.class);

    public HttpHeaders generateHeaderForSuccessPost(HttpServletRequest request, Long userId) {

        HttpHeaders httpHeaders = new HttpHeaders();

        try {
            httpHeaders.setLocation(new URI(request.getRequestURL() + "/" + userId));
        } catch (URISyntaxException e) {
            log.error("Error generating URI", e);
        }

        httpHeaders.add("Content-Type", "application/json");
        return httpHeaders;

    }

    public HttpHeaders generateHeaderForSuccessPut(HttpServletRequest request, Long userId) {
        HttpHeaders httpHeaders = new HttpHeaders();

        try {
          httpHeaders.setLocation(new URI(request.getRequestURL() + "/" + userId));
        } catch (URISyntaxException e) {
            log.error("Error generating URI", e);
        }

        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Cache-Control", "no-cache, no-store, must-revalidate");
        httpHeaders.add("Last-Modified", LocalDateTime.now().toString());

        return httpHeaders;
    }
}
