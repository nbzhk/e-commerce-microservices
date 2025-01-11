package com.example.usermicroservice.service;

import com.example.usermicroservice.exeption.UserNotFoundException;
import com.example.usermicroservice.exeption.UserRegistrationFailedException;
import com.example.usermicroservice.model.dto.UserDetailsDTO;
import com.example.usermicroservice.model.dto.UserLoginDTO;
import com.example.usermicroservice.model.dto.UserRegistrationDTO;
import com.example.usermicroservice.model.dto.UserResponseDTO;
import com.example.usermicroservice.model.entity.UserDetailsEntity;
import com.example.usermicroservice.model.entity.UserEntity;
import com.example.usermicroservice.model.enums.UserRoleEnum;
import com.example.usermicroservice.repo.UserDetailsRepository;
import com.example.usermicroservice.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, UserDetailsRepository userDetailsRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserResponseDTO register(UserRegistrationDTO userRegistrationDTO) {

        try {

            UserEntity newUser = this.modelMapper.map(userRegistrationDTO, UserEntity.class);

            UserDetailsEntity newUserDetails = new UserDetailsEntity();
            newUserDetails.setUser(newUser);

            newUser.setRoles(List.of(UserRoleEnum.USER));
            newUser.setUserDetails(newUserDetails);


            this.userRepository.save(newUser);

            return this.modelMapper.map(newUser, UserResponseDTO.class);

        } catch (UserRegistrationFailedException e) {

            log.error(e.getMessage());
            throw new UserRegistrationFailedException("User registration failed.");
        }
    }


    @Override
    public void updateDetails(Long id, UserDetailsDTO userDetailsDTO) {
        this.userDetailsRepository.findByUserId(id).ifPresent(currentUserDetails -> {
            currentUserDetails.updateDetails(userDetailsDTO);
            this.userDetailsRepository.save(currentUserDetails);
        });
    }

    @Override
    public UserDetailsDTO getUserDetails(Long id) {
        Optional<UserEntity> user = this.userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }

        return this.modelMapper.map(user.get().getUserDetails(), UserDetailsDTO.class);
    }

    @Override
    public UserLoginDTO findByUsername(String username) {
        Optional<UserEntity> byUsername = this.userRepository.findByUsername(username);

        if (byUsername.isEmpty()) {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }

        return modelMapper.map(byUsername.get(), UserLoginDTO.class);
    }

    @Override
    public void promoteToAdmin(String username) {
        try {
            this.userRepository.findByUsername(username).ifPresent(currentUser -> {
                List<UserRoleEnum> currentRoles = currentUser.getRoles();
                currentRoles.add(UserRoleEnum.ADMIN);
                currentUser.setRoles(currentRoles);
                this.userRepository.save(currentUser);
            });

        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            throw new UserNotFoundException("User with username " + username + " not found.");
        }
    }

    @Override
    public void deleteUser(Long id) {

        Optional<UserEntity> user = this.userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User with id: " + id + " not found.");
        }

        try {
            this.userDetailsRepository.deleteById(id);

        } catch (Exception e) {
            log.error("Error deleting user with id: {}", id, e);

            throw new RuntimeException("Failed to delete user");
        }

    }

}
