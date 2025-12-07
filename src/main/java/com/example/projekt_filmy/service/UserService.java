package com.example.projekt_filmy.service;

import com.example.projekt_filmy.model.User;

import java.util.Optional;

public interface UserService {
    User register(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}