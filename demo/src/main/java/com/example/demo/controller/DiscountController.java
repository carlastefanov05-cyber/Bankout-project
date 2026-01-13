package com.example.demo.controller;

import com.example.demo.model.Discount;
import com.example.demo.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/discounts")
@CrossOrigin(origins = "*")
public class DiscountController {

    @Autowired
    private DiscountRepository discountRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Discount>> getMyDiscounts(@PathVariable Long userId) {
        return ResponseEntity.ok(discountRepository.findByUserIdAndAvailableTrue(userId));
    }

    @PostMapping("/redeem/{discountId}")
    public ResponseEntity<String> redeemDiscount(@PathVariable Long discountId) {
        return discountRepository.findById(discountId).map(discount -> {
            discount.setAvailable(false);
            discountRepository.save(discount);
            return ResponseEntity.ok("Discount redeemed successfully!");
        }).orElse(ResponseEntity.notFound().build());
    }
}