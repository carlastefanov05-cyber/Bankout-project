package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void deleteAccount(Long userId) {
        userRepository.deleteById(userId);
    }

    public void updateProfile(Long userId, String newPhone, String newAddress) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setPhone(newPhone);
            user.setAddress(newAddress);
            userRepository.save(user);
        });
    }
}