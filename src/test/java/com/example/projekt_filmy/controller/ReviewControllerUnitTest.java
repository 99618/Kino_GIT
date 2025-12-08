package com.example.projekt_filmy.controller;

import com.example.projekt_filmy.service.MovieService;
import com.example.projekt_filmy.service.ReviewService;
import com.example.projekt_filmy.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

class ReviewControllerUnitTest {

    ReviewService reviewService = Mockito.mock(ReviewService.class);
    MovieService movieService = Mockito.mock(MovieService.class);
    UserService userService = Mockito.mock(UserService.class);

    ReviewController controller = new ReviewController(reviewService, movieService, userService);
    MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

    @Test
    void postReview_withoutAuth_returns401() throws Exception {
        ObjectMapper json = new ObjectMapper();
        mvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of("movieId", 1, "rating", 5))))
                .andExpect(status().isUnauthorized());
    }
}