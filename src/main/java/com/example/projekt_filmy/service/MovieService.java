package com.example.projekt_filmy.service;

import com.example.projekt_filmy.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> getAllMovies();
    Optional<Movie> findById(Long id);
}