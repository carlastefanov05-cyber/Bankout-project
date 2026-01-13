package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String transactionType;
    private Double amount;
    private LocalDateTime date;
    private String description;

    public Transaction(User user, String type, Double amount, String description) {
        this.user = user;
        this.transactionType = type;
        this.amount = amount;
        this.description = description;
        this.date = LocalDateTime.now();
    }
}