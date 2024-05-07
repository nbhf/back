package com.example.SpringMongoProject.Controller;


import com.example.SpringMongoProject.Model.AuthUser;
import com.example.SpringMongoProject.Repo.AuthUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody AuthUser user){
        try {
            if (userRepository.findByUsername(user.getUsername()).isPresent())
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken. Please try again");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            AuthUser save = userRepository.save(user);
            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthUser user) {
        Optional<AuthUser> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            AuthUser existingUser = optionalUser.get();
            if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                return ResponseEntity.ok("Login successful!");
            } else {
                // Password does not match
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }
        } else {
            // User not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


    // Read operation
    @GetMapping("/users")
    public ResponseEntity<List<AuthUser>> getAllUsers() {
        List<AuthUser> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AuthUser> getUserById(@PathVariable String id) {
        AuthUser user = userRepository.findById(id)
                .orElse(null); // You can handle the case when the user is not found
        return ResponseEntity.ok(user);
    }

    // Update operation
    @PutMapping("/users/{id}")
    public ResponseEntity updateUser(@PathVariable String id, @RequestBody AuthUser user) {
        AuthUser existingUser = userRepository.findById(id)
                .orElse(null); // You can handle the case when the user is not found
        if (existingUser != null) {
            // Update the fields you want to update
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            // Save the updated user
            userRepository.save(existingUser);
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete operation
    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }



}
