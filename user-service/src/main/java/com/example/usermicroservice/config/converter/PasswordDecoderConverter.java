package com.example.usermicroservice.config.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordDecoderConverter implements Converter<String, String> {

    @Override
    public String convert(MappingContext<String, String> mappingContext) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.encode(mappingContext.getSource());
    }
}
