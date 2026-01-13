package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long accountId) {
        try {
            userService.deleteAccount(accountId);
            return ResponseEntity.ok("User account deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Error: User not found.");
        }
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<User> getUserProfile(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{accountId}")
    public ResponseEntity<String> updateUser(@PathVariable Long accountId, @RequestBody User updateData) {
        try {

            userService.updateProfile(accountId, updateData.getPhone(), updateData.getAddress());
            return ResponseEntity.ok("Profile updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Error: Could not update profile.");
        }
    }
}