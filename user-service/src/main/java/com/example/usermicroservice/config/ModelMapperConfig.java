package com.example.usermicroservice.config;

import com.example.usermicroservice.config.converter.PasswordDecoderConverter;
import com.example.usermicroservice.model.dto.UserLoginDTO;
import com.example.usermicroservice.model.dto.UserRegistrationDTO;
import com.example.usermicroservice.model.dto.UserResponseDTO;
import com.example.usermicroservice.model.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Objects;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(UserRegistrationDTO.class, UserEntity.class)
                .addMappings(mapping -> mapping.using(new PasswordDecoderConverter())
                .map(UserRegistrationDTO::getPassword, UserEntity::setPassword));

        TypeMap<UserEntity, UserResponseDTO> typeMap = modelMapper.createTypeMap(UserEntity.class, UserResponseDTO.class);
        typeMap.addMapping(u -> u.getUserDetails().getFirstName(), UserResponseDTO::setFirstName);
        typeMap.addMapping(u -> u.getUserDetails().getLastName(), UserResponseDTO::setLastName);
        typeMap.addMapping(u -> u.getUserDetails().getPhoneNumber(), UserResponseDTO::setPhoneNumber);
        typeMap.addMapping(u -> u.getUserDetails().getAddress(), UserResponseDTO::setAddress);
        typeMap.addMapping(u -> u.getUserDetails().getCity(), UserResponseDTO::setCity);
        typeMap.addMapping(u -> u.getUserDetails().getCountry(), UserResponseDTO::setCountry);
        typeMap.addMapping(u -> u.getUserDetails().getZip(), UserResponseDTO::setZip);
        typeMap.addMapping(UserEntity::getRoles, UserResponseDTO::setRoles);

        TypeMap<UserEntity, UserLoginDTO> loginMap = modelMapper.createTypeMap(UserEntity.class, UserLoginDTO.class);
        loginMap.addMapping(
                u -> u.getRoles() == null ? new ArrayList<>() : u.getRoles().stream()
                        .map(Objects::toString)
                        .toList(),
                UserLoginDTO::setRoles
        );


        return modelMapper;
    }

}
