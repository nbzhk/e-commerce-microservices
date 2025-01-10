package com.example.usermicroservice.service;

import com.example.usermicroservice.model.dto.UserDetailsDTO;
import com.example.usermicroservice.model.dto.UserLoginDTO;
import com.example.usermicroservice.model.dto.UserRegistrationDTO;
import com.example.usermicroservice.model.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO register(UserRegistrationDTO userRegistrationDTO);

    void deleteUser(Long id);

    void updateDetails(Long id, UserDetailsDTO userDetailsDTO);


    UserDetailsDTO getUserDetails(Long id);

    UserLoginDTO findByUsername(String username);
}
