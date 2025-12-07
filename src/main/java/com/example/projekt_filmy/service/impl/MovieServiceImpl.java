package com.example.projekt_filmy.service.impl;

import com.example.projekt_filmy.model.Movie;
import com.example.projekt_filmy.repository.MovieRepository;
import com.example.projekt_filmy.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository repo;

    @Override
    public List<Movie> getAllMovies() { return repo.findAll(); }

    @Override
    public Optional<Movie> findById(Long id) { return repo.findById(id); }
}