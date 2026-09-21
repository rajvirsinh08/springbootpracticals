package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.AppUser;
import com.example.demo.repository.AppUserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository repository,PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppUser user) {

        // Check username already exists
        if (repository.findByUsername(user.getUsername()).isPresent()) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Convert plain password to BCrypt
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        // Save user
        AppUser savedUser = repository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    // UPDATE PROFILE
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody AppUser data,Authentication authentication) {

        // Get logged-in username
        String username = authentication.getName();

        // Find user
        AppUser user = repository.findByUsername(username).orElseThrow();

        // Update username
        if (data.getUsername() != null) {

            user.setUsername(data.getUsername());
        }

        // Update role
        if (data.getRole() != null) {

            user.setRole(data.getRole());
        }

        repository.save(user);

        return ResponseEntity.ok(user);
    }


    // DELETE ACCOUNT
    @DeleteMapping("/deregister")
    public ResponseEntity<Void> deregister(Authentication authentication) {

        // Get logged-in username
        String username = authentication.getName();

        // Find user
        AppUser user = repository.findByUsername(username).orElseThrow();

        // Delete
        repository.delete(user);

        return ResponseEntity.noContent().build();
    }
}