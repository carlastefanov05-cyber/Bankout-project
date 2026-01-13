package com.example.demo.repository;

import com.example.demo.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByUserIdAndAvailableTrue(Long userId);
}