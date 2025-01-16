package com.example.usermicroservice.controller;

import com.example.usermicroservice.model.dto.UserDetailsDTO;
import com.example.usermicroservice.model.dto.UserLoginDTO;
import com.example.usermicroservice.model.dto.UserRegistrationDTO;
import com.example.usermicroservice.model.dto.UserResponseDTO;
import com.example.usermicroservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void testRegisterUser_ValidInput_ReturnsCreated() throws Exception {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setUsername("testuser");
        userRegistrationDTO.setPassword("testpassword");
        userRegistrationDTO.setEmail("email@example.com");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("testuser");
        userResponseDTO.setEmail("email@example.com");

        when(this.userService.register(any(UserRegistrationDTO.class))).thenReturn(userResponseDTO);

        this.mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("email@example.com"));

    }

    @Test
    void testUpdateUserDetails_ValidInput_ReturnsOk() throws Exception {
        Long userId = 1L;
        UserDetailsDTO detailsDTO = new UserDetailsDTO();
        detailsDTO.setFirstName("Test_NAME");
        detailsDTO.setLastName("Test_LAST_NAME");

        doNothing().when(this.userService).updateDetails(eq(userId), any(UserDetailsDTO.class));

        this.mockMvc.perform(put("/api/users/update/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detailsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test_NAME"))
                .andExpect(jsonPath("$.lastName").value("Test_LAST_NAME"));
    }

    @Test
    void testGetUserDetails_ExistingUser_ReturnsOk() throws Exception {
        Long userId = 1L;
        UserDetailsDTO detailsDTO = new UserDetailsDTO();
        detailsDTO.setFirstName("Test_NAME");
        detailsDTO.setLastName("Test_LAST_NAME");


        when(this.userService.getUserDetails(userId)).thenReturn(detailsDTO);

        mockMvc.perform(get("/api/users/details/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test_NAME"))
                .andExpect(jsonPath("$.lastName").value("Test_LAST_NAME"));
    }

    @Test
    void testDeleteUser_ExistingUser_ReturnsNoContent() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);


        this.mockMvc.perform(delete("/api/users/delete/{id}", userId))
                .andExpect(status().isNoContent());

        verify(this.userService).deleteUser(userId);
    }

    @Test
    void testFindByUsername_ExistingUser_ReturnsOk() throws Exception {
        String username = "testuser";
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername(username);

        when(this.userService.findByUsername(username)).thenReturn(loginDTO);

        this.mockMvc.perform(get("/api/users/find/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void testPromoteUserToAdmin_ExistingUser_ReturnsOk() throws Exception {
        String username = "testuser";
        doNothing().when(userService).promoteToAdmin(username);

        this.mockMvc.perform(put("/api/users/promote/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().string("User " + username + " is now admin"));

        verify(this.userService).promoteToAdmin(username);
    }

    @Test
    void testRegisterUser_InvalidInput_ReturnsBadRequest() throws Exception {
        UserRegistrationDTO invalidDTO = new UserRegistrationDTO();

        this.mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }
}
