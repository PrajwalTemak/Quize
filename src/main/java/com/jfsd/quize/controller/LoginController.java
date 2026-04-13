package com.jfsd.quize.controller;

import com.jfsd.quize.dto.AddUserRequest;
import com.jfsd.quize.dto.LoginRequest;
import com.jfsd.quize.entity.User;
import com.jfsd.quize.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    // ADD USER
    @PostMapping("/add-user")
    public ResponseEntity<String> addUser(@RequestBody AddUserRequest request) {

        User user = new User();
        user.setId(request.getId());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword());
        user.setRole(request.getRole());
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok("User added successfully");
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (!optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = optionalUser.get();

        if (!user.getPasswordHash().equals(request.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect password");
        }

        if (!user.getIsActive()) {
            return ResponseEntity.badRequest().body("User account inactive");
        }

        return ResponseEntity.ok("Login successful as " + user.getRole());
    }
}