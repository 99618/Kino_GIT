package com.example.projekt_filmy.repository;

import com.example.projekt_filmy.model.Review;
import com.example.projekt_filmy.model.Movie;
import com.example.projekt_filmy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMovie(Movie movie);
    List<Review> findByUser(User user);
    Optional<Review> findByUserAndMovie(User user, Movie movie);
}