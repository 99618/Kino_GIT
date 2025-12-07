package com.example.projekt_filmy.service.impl;

import com.example.projekt_filmy.model.Movie;
import com.example.projekt_filmy.model.Review;
import com.example.projekt_filmy.model.User;
import com.example.projekt_filmy.repository.ReviewRepository;
import com.example.projekt_filmy.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repo;

    @Override
    public Review add(Review review) {
        if (review.getUser() != null && review.getMovie() != null) {
            Optional<Review> existing = repo.findByUserAndMovie(review.getUser(), review.getMovie());
            if (existing.isPresent() && (review.getId() == null || !existing.get().getId().equals(review.getId())))
                throw new IllegalStateException("Użytkownik już ocenił ten film.");
        }
        return repo.save(review);
    }

    @Override
    public Optional<Review> findById(Long id) { return repo.findById(id); }

    @Override
    public Optional<Review> findByUserAndMovie(User user, Movie movie) { return repo.findByUserAndMovie(user, movie); }

    @Override
    public List<Review> getByMovie(Movie movie) { return repo.findByMovie(movie); }

    @Override
    public void deleteById(Long id) { repo.deleteById(id); }
}