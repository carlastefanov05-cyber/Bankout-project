package com.example.demo.controller;

import com.example.demo.model.Transaction;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(userId));
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody Map<String, Object> payload) {

        if (!payload.containsKey("userId") || payload.get("userId") == null) {
            return ResponseEntity.badRequest().body("Missing userId");
        }

        Long userId;
        try {
            userId = Long.valueOf(payload.get("userId").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid userId");
        }

        Double amount = Double.valueOf(payload.get("amount").toString());
        transactionService.deposit(userId, amount);
        return ResponseEntity.ok("Deposit successful!");
    }


    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Double amount = Double.valueOf(payload.get("amount").toString());
        boolean success = transactionService.withdraw(userId, amount);
        return success ? ResponseEntity.ok("Withdrawal successful!")
                : ResponseEntity.badRequest().body("Insufficient funds.");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody Map<String, Object> payload) {
        Long senderId = Long.valueOf(payload.get("senderId").toString());
        Long receiverId = Long.valueOf(payload.get("receiverId").toString());
        Double amount = Double.valueOf(payload.get("amount").toString());
        boolean success = transactionService.transfer(senderId, receiverId, amount);
        return success ? ResponseEntity.ok("Transfer successful!")
                : ResponseEntity.badRequest().body("Transfer failed (Insufficient funds).");
    }
}