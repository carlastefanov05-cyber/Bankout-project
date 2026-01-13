package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public record RegisterRequest(String username, String email, String password, String name, String address, String phone) {}
    public record LoginRequest(String email, String password) {}
    public record Verify2FARequest(String email, String code) {}

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            authService.registerUser(
                    request.username(),
                    request.email(),
                    request.password(),
                    request.name(),
                    request.address(),
                    request.phone()
            );
            return ResponseEntity.ok("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        // Attempt login + send 2FA
        boolean success = authService.attemptLoginAndSend2FA(request.email(), request.password());

        if (!success) {
            Map<String,Object> error = new HashMap<>();
            error.put("error", "Invalid email or password");
            return ResponseEntity.status(401).body(error);
        }

        User user = authService.findByEmail(request.email()).orElse(null);

        if (user == null) {
            Map<String,Object> error = new HashMap<>();
            error.put("error", "User not found");
            return ResponseEntity.status(401).body(error);
        }


        Map<String,Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/2fa/verify")
    public ResponseEntity<String> verify2FA(@RequestBody Verify2FARequest request) {
        if (authService.verify2FACode(request.email(), request.code())) {
            return ResponseEntity.ok("Authentication successful! Welcome to Bankout.");
        }
        return ResponseEntity.badRequest().body("Invalid or expired 2FA code.");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("The Bankout server is up and running!");
    }
}