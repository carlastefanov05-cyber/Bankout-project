package com.example.demo.service;

import com.example.demo.model.Transaction;
import com.example.demo.model.User;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Transaction> getTransactionHistory(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Transactional
    public void deposit(Long userId, Double amount) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        Transaction trans = new Transaction(user, "DEPOSIT", amount, "Cash deposit");
        transactionRepository.save(trans);
    }

    @Transactional
    public boolean withdraw(Long userId, Double amount) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getBalance() < amount) return false;

        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);

        Transaction trans = new Transaction(user, "WITHDRAW", amount, "Cash withdrawal");
        transactionRepository.save(trans);
        return true;
    }

    @Transactional
    public boolean transfer(Long userId, Long otherUserId, Double amount) {
        User user = userRepository.findById(userId).orElseThrow();
        User user2 = userRepository.findById(otherUserId).orElseThrow();
        if (user.getBalance() < amount) return false;
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
        Transaction trans = new Transaction(user, "TRANSFER", amount, "Cash sent");
        user2.setBalance(user2.getBalance() + amount);
        userRepository.save(user2);
        Transaction trans2 = new Transaction(user2, "TRANSFER", amount, "Cash received");
        transactionRepository.save(trans);
        transactionRepository.save(trans2);
        return true;
    }
}