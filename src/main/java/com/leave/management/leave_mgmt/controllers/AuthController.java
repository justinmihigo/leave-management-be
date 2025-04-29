package com.leave.management.leave_mgmt.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.leave.management.leave_mgmt.services.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.security.Key;
import io.jsonwebtoken.security.Keys;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String MESSAGE_KEY = "message"; // Define a constant for the message key

    private static final String JWT_SECRET_KEY = "yourSecureSecretKeyForJWTGeneration"; // Hardcoded secret key

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(MESSAGE_KEY, "Missing email or password"));
        }

        boolean isAuthenticated = authService.authenticate(email, password);
        if (isAuthenticated) {
            // Generate JWT token using a secure Key
            Key secretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes()); // Replace with a securely stored key
            String token = Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();

            return ResponseEntity.ok(Map.of(
                MESSAGE_KEY, "Login successful",
                "email", email,
                "userId", authService.getUserIdByEmail(email), // Fetch userId from AuthService
                "token", token
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(MESSAGE_KEY, "Invalid credentials"));
        }
    }
}