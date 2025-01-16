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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationDTO userRegistrationDTO;
    private UserEntity userEntity;
    private UserDetailsEntity userDetailsEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.userRegistrationDTO = new UserRegistrationDTO();
        this.userRegistrationDTO.setUsername("test_username");
        this.userRegistrationDTO.setPassword("test_password");

        this.userEntity = new UserEntity();
        this.userEntity.setUsername("test_username");
        this.userEntity.setId(1L);

        this.userDetailsEntity = new UserDetailsEntity();
        this.userDetailsEntity.setUser(this.userEntity);

    }

    @Test
    void testRegister_ShouldSaveUserAndReturnResponse() {
        UserResponseDTO expectedResponse = new UserResponseDTO();
        expectedResponse.setUsername("test_username");
        expectedResponse.setId(101L);

        when(modelMapper.map(userRegistrationDTO, UserEntity.class)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserResponseDTO.class)).thenReturn(expectedResponse);

        UserResponseDTO actualResponse = this.userService.register(userRegistrationDTO);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        verify(this.userRepository, times(1)).save(userEntity);
    }

    @Test
    void testRegister_shouldThrowUserRegistrationFailedException() {
        when(modelMapper.map(userRegistrationDTO, UserEntity.class)).thenThrow(UserRegistrationFailedException.class);

        assertThrows(UserRegistrationFailedException.class, () -> this.userService.register(userRegistrationDTO));
    }

    @Test
    void testUpdateDetails_ShouldUpdateUserDetails() {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setFirstName("test_name");
        userDetailsDTO.setLastName("test_name");
        userDetailsDTO.setPhoneNumber("00000000");
        userDetailsDTO.setAddress("test_street");
        userDetailsDTO.setCity("test_city");
        userDetailsDTO.setCountry("test_country");
        userDetailsDTO.setZip("test_zip");

        when(this.userDetailsRepository.findByUserId(1L)).thenReturn(Optional.of(userDetailsEntity));

        this.userService.updateDetails(1L, userDetailsDTO);

        verify(this.userDetailsRepository, times(1)).save(userDetailsEntity);
        assertEquals(userDetailsDTO.getFirstName(), this.userDetailsEntity.getFirstName());
        assertEquals(userDetailsDTO.getLastName(), this.userDetailsEntity.getLastName());
        assertEquals(userDetailsDTO.getPhoneNumber(), this.userDetailsEntity.getPhoneNumber());
        assertEquals(userDetailsDTO.getAddress(), this.userDetailsEntity.getAddress());
        assertEquals(userDetailsDTO.getCity(), this.userDetailsEntity.getCity());
        assertEquals(userDetailsDTO.getCountry(), this.userDetailsEntity.getCountry());
        assertEquals(userDetailsDTO.getZip(), this.userDetailsEntity.getZip());
    }

    @Test
    void testGetUserDetails_ShouldReturnUserDetails() {
        UserDetailsDTO expected = new UserDetailsDTO();
        expected.setFirstName("test_name");
        expected.setLastName("test_name");
        expected.setPhoneNumber("00000000");
        expected.setAddress("test_street");
        expected.setCity("test_city");
        expected.setCountry("test_country");
        expected.setZip("test_zip");

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(this.modelMapper.map(userEntity.getUserDetails(), UserDetailsDTO.class)).thenReturn(expected);

        UserDetailsDTO actual = this.userService.getUserDetails(1L);

        assertNotNull(actual);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getZip(), actual.getZip());
    }

    @Test
    void testGetUserDetails_shouldThrowUserNotFoundException() {
        when(this.userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> this.userService.getUserDetails(1L));
    }

    @Test
    void testFindByUsername_shouldReturnUserLoginDTO() {
        UserLoginDTO expectedLogin = new UserLoginDTO();
        expectedLogin.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, UserLoginDTO.class)).thenReturn(expectedLogin);

        UserLoginDTO actualLogin = this.userService.findByUsername("testUser");

        assertNotNull(actualLogin);
        assertEquals(expectedLogin.getUsername(), actualLogin.getUsername());
    }

    @Test
    void testFindByUsername_shouldThrowUserNotFoundException() {
        when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> this.userService.findByUsername("testUser"));
    }

    @Test
    void testPromoteToAdmin_shouldAddAdminRole() {
        this.userEntity.setRoles(new ArrayList<>(List.of(UserRoleEnum.USER)));
        when(this.userRepository.findByUsername("test_user")).thenReturn(Optional.of(this.userEntity));

        this.userService.promoteToAdmin("test_user");

        verify(this.userRepository, times(1)).save(this.userEntity);
        assertTrue(this.userEntity.getRoles().contains(UserRoleEnum.ADMIN));
    }

    @Test
    void testDeleteUser_shouldDeleteUser() {
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        this.userService.deleteUser(1L);

        verify(this.userDetailsRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_shouldThrowUserNotFoundException() {
        when(this.userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> this.userService.deleteUser(1L));
    }



}
