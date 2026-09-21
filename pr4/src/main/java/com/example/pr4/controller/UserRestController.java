package com.example.pr4.controller;

import com.example.pr4.entity.User;
import com.example.pr4.entity.UserProfile;
import com.example.pr4.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody User user) {

        if (user.getUserProfile() != null) {
            user.getUserProfile().setUser(user);
        }

        User savedUser = userRepository.save(user);

        return new ResponseEntity<>(
                savedUser,
                HttpStatus.CREATED
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(
                userRepository.findAll()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable int id) {

        Optional<User> user =
                userRepository.findById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        return ResponseEntity.notFound().build();
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable int id,
            @RequestBody User userDetails) {

        Optional<User> existingUser =
                userRepository.findById(id);

        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = existingUser.get();

        if (userDetails.getUsername() != null) {
            user.setUsername(userDetails.getUsername());
        }

        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }

        UserProfile newProfile =
                userDetails.getUserProfile();

        if (newProfile != null) {

            UserProfile profile =
                    user.getUserProfile();

            if (profile == null) {
                profile = newProfile;
                profile.setUser(user);
                user.setUserProfile(profile);
            } else {

                if (newProfile.getAddress() != null) {
                    profile.setAddress(
                            newProfile.getAddress()
                    );
                }

                if (newProfile.getPhone() != null) {
                    profile.setPhone(
                            newProfile.getPhone()
                    );
                }

                if (newProfile.getDob() != null) {
                    profile.setDob(
                            newProfile.getDob()
                    );
                }
            }
        }

        User updatedUser =
                userRepository.save(user);

        return ResponseEntity.ok(updatedUser);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable int id) {

        Optional<User> user =
                userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}