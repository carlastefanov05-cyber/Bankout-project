package com.example.demo.controller;

import com.example.demo.service.EmailService;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/alerts")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/lowbalance")
    public ResponseEntity<String> sendLowBalanceAlert(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());

        return userRepository.findById(userId).map(user -> {
            String subject = "Bankout Alert: Low Balance";
            String body = "Hello " + user.getName() + ",\n\nYour account balance has dropped below the safety threshold. Current balance: $" + user.getBalance() + ". Please deposit funds to avoid fees.";

            emailService.sendEmail(user.getEmail(), user.getUsername(), body);
            return ResponseEntity.ok("Low balance alert sent to " + user.getEmail());
        }).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/suspicious")
    public ResponseEntity<String> sendSuspiciousAlert(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());

        return userRepository.findById(userId).map(user -> {
            String subject = "SECURITY ALERT: Suspicious Activity";
            String body = "Hello " + user.getName() + ",\n\nWe detected an unusual login or transaction attempt on your Bankout account. If this was not you, please freeze your account immediately.";

            emailService.sendEmail(user.getEmail(), user.getUsername(), body);
            return ResponseEntity.ok("Security alert sent to " + user.getEmail());
        }).orElse(ResponseEntity.notFound().build());
    }
}