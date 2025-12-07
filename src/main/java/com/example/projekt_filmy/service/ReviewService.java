package com.example.projekt_filmy.service;

import com.example.projekt_filmy.model.Movie;
import com.example.projekt_filmy.model.Review;
import com.example.projekt_filmy.model.User;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Review add(Review review);
    Optional<Review> findById(Long id);
    Optional<Review> findByUserAndMovie(User user, Movie movie);
    List<Review> getByMovie(Movie movie);
    void deleteById(Long id);
}