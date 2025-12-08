package com.example.projekt_filmy.service;

import com.example.projekt_filmy.model.User;
import com.example.projekt_filmy.repository.UserRepository;
import com.example.projekt_filmy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    UserRepository repo = Mockito.mock(UserRepository.class);
    BCryptPasswordEncoder encoder = Mockito.mock(BCryptPasswordEncoder.class);
    UserServiceImpl svc = new UserServiceImpl(repo, encoder);

    @Test
    void register_encodesPasswordAndSaves() {
        User u = new User(); u.setPassword("plain");
        Mockito.when(encoder.encode("plain")).thenReturn("hash");
        Mockito.when(repo.save(u)).thenReturn(u);

        var saved = svc.register(u);
        Mockito.verify(encoder).encode("plain");
        Mockito.verify(repo).save(u);
        assertThat(saved).isNotNull();
    }

    @Test
    void findByEmail_delegatesToRepo() {
        Mockito.when(repo.findByEmail("a")).thenReturn(Optional.empty());
        assertThat(svc.findByEmail("a")).isEmpty();
    }
}