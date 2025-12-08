package com.example.projekt_filmy.controller;

import com.example.projekt_filmy.model.Movie;
import com.example.projekt_filmy.model.Review;
import com.example.projekt_filmy.model.User;
import com.example.projekt_filmy.service.MovieService;
import com.example.projekt_filmy.service.ReviewService;
import com.example.projekt_filmy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final MovieService movieService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Map<String, Object> body,
                                 Authentication auth) {

        if (auth == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Brak autoryzacji"));
        }

        User user = userService.findByEmail(auth.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Nie znaleziono użytkownika"));
        }

        Long movieId;
        int rating;
        try {
            movieId = Long.valueOf(body.get("movieId").toString());
            rating = Integer.parseInt(body.get("rating").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Niepoprawne dane wejściowe"));
        }

        String text = body.get("text") == null ? null : body.get("text").toString();

        Movie movie = movieService.findById(movieId).orElse(null);
        if (movie == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Nie znaleziono filmu"));
        }

        if (reviewService.findByUserAndMovie(user, movie).isPresent()) {
            return ResponseEntity.status(400).body(Map.of("message", "Użytkownik już ocenił ten film"));
        }

        Review r = Review.builder()
                .user(user)
                .movie(movie)
                .rating(rating)
                .text(text)
                .build();

        Review saved = reviewService.add(r);
        return ResponseEntity.status(201).body(Map.of("message", "Dodano recenzję", "review", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id,
                                  @RequestBody Map<String, Object> body,
                                  Authentication auth) {

        if (auth == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Brak autoryzacji"));
        }

        User user = userService.findByEmail(auth.getName()).orElse(null);
        Review rev = reviewService.findById(id).orElse(null);
        if (rev == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Nie znaleziono recenzji"));
        }

        boolean owner = rev.getUser() != null && user != null && rev.getUser().getId().equals(user.getId());
        boolean admin = user != null && user.getRole() == User.Role.ADMIN;
        if (!(owner || admin)) {
            return ResponseEntity.status(403).body(Map.of("message", "Brak uprawnień"));
        }

        try {
            int rating = Integer.parseInt(body.get("rating").toString());
            rev.setRating(rating);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Niepoprawna ocena"));
        }

        rev.setText(body.get("text") == null ? null : body.get("text").toString());
        Review updated = reviewService.add(rev);

        return ResponseEntity.ok(Map.of("message", "Zedytowano recenzję", "review", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    Authentication auth) {

        if (auth == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Brak autoryzacji"));
        }

        User user = userService.findByEmail(auth.getName()).orElse(null);
        Review rev = reviewService.findById(id).orElse(null);
        if (rev == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Nie znaleziono recenzji"));
        }

        boolean owner = rev.getUser() != null && user != null && rev.getUser().getId().equals(user.getId());
        boolean admin = user != null && user.getRole() == User.Role.ADMIN;
        if (!(owner || admin)) {
            return ResponseEntity.status(403).body(Map.of("message", "Brak uprawnień"));
        }

        reviewService.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Usunięto recenzję"));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> byMovie(@PathVariable Long movieId) {
        Movie movie = movieService.findById(movieId).orElse(null);
        if (movie == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Nie znaleziono filmu"));
        }
        List<Review> list = reviewService.getByMovie(movie);
        return ResponseEntity.ok(list);
    }
}