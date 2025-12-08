package com.example.projekt_filmy.service;

import com.example.projekt_filmy.model.Movie;
import com.example.projekt_filmy.repository.MovieRepository;
import com.example.projekt_filmy.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MovieServiceTest {

    MovieRepository repo = Mockito.mock(MovieRepository.class);
    MovieServiceImpl svc = new MovieServiceImpl(repo);

    @Test
    void getAllMovies_returnsNonEmptyList() {
        Mockito.when(repo.findAll()).thenReturn(List.of(new Movie(){ { setTitle("X"); } }));
        var res = svc.getAllMovies();
        assertThat(res).isNotEmpty();
    }

    @Test
    void findById_returnsPresent() {
        var m = new Movie(); m.setId(1L); m.setTitle("T");
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(m));
        assertThat(svc.findById(1L)).isPresent();
    }
}