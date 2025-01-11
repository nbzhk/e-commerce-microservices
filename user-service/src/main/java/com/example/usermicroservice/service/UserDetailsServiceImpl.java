package com.example.usermicroservice.service;

import com.example.usermicroservice.model.entity.UserEntity;
import com.example.usermicroservice.model.enums.UserRoleEnum;
import com.example.usermicroservice.repo.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(UserDetailsServiceImpl::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " was not found."));
    }

    private static UserDetails map(UserEntity userEntity) {

        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(userEntity.getRoles().stream().map(UserDetailsServiceImpl::mapRoles).toList())
                .build();
    }

    private static GrantedAuthority mapRoles(UserRoleEnum userRoleEnum) {
        return new SimpleGrantedAuthority("ROLE_" + userRoleEnum.name());
    }
}
