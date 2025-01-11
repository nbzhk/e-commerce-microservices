package com.example.usermicroservice.controller;

import com.example.usermicroservice.headers.HeaderGenerator;
import com.example.usermicroservice.model.dto.UserDetailsDTO;
import com.example.usermicroservice.model.dto.UserLoginDTO;
import com.example.usermicroservice.model.dto.UserRegistrationDTO;
import com.example.usermicroservice.model.dto.UserResponseDTO;
import com.example.usermicroservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final HeaderGenerator headerGenerator;

    public UserController(UserService userService, HeaderGenerator headerGenerator) {
        this.userService = userService;
        this.headerGenerator = headerGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO,
                                                        HttpServletRequest request) {

        UserResponseDTO userResponseDTO = this.userService.register(userRegistrationDTO);

        return new ResponseEntity<>(
                userResponseDTO,
                this.headerGenerator.generateHeaderForSuccessPost(request, userResponseDTO.getId()),
                HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDetailsDTO> updateUserDetails(@PathVariable("id") Long id,
                                                            @Valid @RequestBody UserDetailsDTO userDetailsDTO,
                                                            HttpServletRequest request) {

        this.userService.updateDetails(id, userDetailsDTO);

        return new ResponseEntity<>(
                userDetailsDTO,
                this.headerGenerator.generateHeaderForSuccessPut(request, id),
                HttpStatus.OK
        );
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<UserDetailsDTO> getUserDetails(@PathVariable("id") Long id) {

        UserDetailsDTO userDetails = this.userService.getUserDetails(id);

        return ResponseEntity.ok(userDetails);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {

        this.userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<UserLoginDTO> findByUsername(@PathVariable String username) {
        UserLoginDTO byUsername = this.userService.findByUsername(username);

        return ResponseEntity.ok(byUsername);
    }

    @PutMapping("/promote/{username}")
    public ResponseEntity<String> promoteUserToAdmin(@PathVariable String username) {
        this.userService.promoteToAdmin(username);

        return ResponseEntity.ok("User " + username + " is now admin");
    }
}
