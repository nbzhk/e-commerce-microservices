package com.example.usermicroservice.controller;

import com.example.usermicroservice.model.dto.UserDetailsDTO;
import com.example.usermicroservice.model.dto.UserLoginDTO;
import com.example.usermicroservice.model.dto.UserRegistrationDTO;
import com.example.usermicroservice.model.dto.UserResponseDTO;
import com.example.usermicroservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {

        UserResponseDTO userResponseDTO = this.userService.register(userRegistrationDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/details/{id}")
                .build(userResponseDTO.getId());

        return ResponseEntity.created(location)
                .body(userResponseDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDetailsDTO> updateUserDetails(@PathVariable("id") Long id,
                                                            @Valid @RequestBody UserDetailsDTO userDetailsDTO) {

        this.userService.updateDetails(id, userDetailsDTO);

        return ResponseEntity.ok().body(userDetailsDTO);
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
