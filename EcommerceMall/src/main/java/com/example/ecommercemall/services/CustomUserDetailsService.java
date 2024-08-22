package com.example.ecommercemall.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Here, you can fetch the user details from a user service or a database.
        return new org.springframework.security.core.userdetails.User(username, "", new ArrayList<>());
    }
}