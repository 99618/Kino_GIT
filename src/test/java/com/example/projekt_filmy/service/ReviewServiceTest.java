package com.example.projekt_filmy.service;

import com.example.projekt_filmy.model.Movie;
import com.example.projekt_filmy.model.Review;
import com.example.projekt_filmy.model.User;
import com.example.projekt_filmy.repository.ReviewRepository;
import com.example.projekt_filmy.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReviewServiceTest {

    ReviewRepository repo = Mockito.mock(ReviewRepository.class);
    ReviewServiceImpl svc = new ReviewServiceImpl(repo);

    @Test
    void add_throwsWhenDuplicateReviewExists() {
        User u = new User(); u.setId(1L);
        Movie m = new Movie(); m.setId(1L);
        Review existing = new Review(); existing.setId(2L); existing.setUser(u); existing.setMovie(m);
        Mockito.when(repo.findByUserAndMovie(u, m)).thenReturn(Optional.of(existing));

        Review newR = Review.builder().user(u).movie(m).rating(5).text("t").build();
        assertThatThrownBy(() -> svc.add(newR)).isInstanceOf(IllegalStateException.class);
    }
}