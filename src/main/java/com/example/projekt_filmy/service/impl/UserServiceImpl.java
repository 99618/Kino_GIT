package com.example.projekt_filmy.service.impl;

import com.example.projekt_filmy.model.User;
import com.example.projekt_filmy.repository.UserRepository;
import com.example.projekt_filmy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;

    @Override
    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        return repo.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) { return repo.findByEmail(email); }

    @Override
    public boolean existsByEmail(String email) { return repo.existsByEmail(email); }
}