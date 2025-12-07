package com.example.projekt_filmy.controller;

import com.example.projekt_filmy.model.User;
import com.example.projekt_filmy.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.expiration}") private long expiration;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User u){
        if(userService.existsByEmail(u.getEmail())) return ResponseEntity.status(409).body(Map.of("message","Użytkownik o tym emailu już istnieje."));
        userService.register(u);
        return ResponseEntity.status(201).body(Map.of("message","Utworzono użytkownika"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body){
        String email = body.get("email");
        String password = body.get("password");
        var opt = userService.findByEmail(email);
        if(opt.isEmpty()) return ResponseEntity.status(401).body(Map.of("message","Błedny mail"));
        var u = opt.get();
        if(!encoder.matches(password, u.getPassword())) return ResponseEntity.status(401).body(Map.of("message","Błędne hasło."));
        var key = Keys.hmacShaKeyFor(secret.getBytes());
        String token = Jwts.builder()
                .setSubject(u.getEmail())
                .claim("role", u.getRole().name())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return ResponseEntity.ok(Map.of("token", token));
    }
}