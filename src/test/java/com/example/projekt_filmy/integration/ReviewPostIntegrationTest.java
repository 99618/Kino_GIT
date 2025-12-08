package com.example.projekt_filmy.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.projekt_filmy.model.User;
import com.example.projekt_filmy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReviewPostIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired UserRepository userRepo;
    @Autowired BCryptPasswordEncoder encoder;

    String token;

    @BeforeEach
    void setup() throws Exception {
        userRepo.deleteAll();
        User u = new User(); u.setEmail("test@gmail.com"); u.setFirstName("testName"); u.setLastName("testLast"); u.setPassword(encoder.encode("test"));
        userRepo.save(u);

        var res = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of("email","test@gmail.com","password","test"))))
                .andExpect(status().isOk()).andReturn();

        token = json.readTree(res.getResponse().getContentAsString()).get("token").asText();
    }

    @Test
    void postReview_authenticated_returns201() throws Exception {
        mvc.perform(post("/api/reviews")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of("movieId", 1, "rating", 8, "text", "ok"))))
                .andExpect(status().isCreated());
    }
}