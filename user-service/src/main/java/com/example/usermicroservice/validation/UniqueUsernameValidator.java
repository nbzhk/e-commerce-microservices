package com.example.usermicroservice.validation;

import com.example.usermicroservice.repo.UserRepository;
import com.example.usermicroservice.validation.annotations.UniqueUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository userRepository;

    public UniqueUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return !this.userRepository.existsByUsername(username);
    }
}
