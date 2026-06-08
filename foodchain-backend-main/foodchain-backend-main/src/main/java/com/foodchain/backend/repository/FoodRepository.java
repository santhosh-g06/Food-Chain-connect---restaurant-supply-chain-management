package com.foodchain.backend.repository;

import com.foodchain.backend.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<FoodItem, Long> {
    
    // Your existing method (if you have it)
    List<FoodItem> findByStatus(String status);

    // THE NEW SMART QUERY: Finds available food expiring between 'now' and 'now + 12 hours', sorted by closest expiry
    List<FoodItem> findByStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
            String status, 
            LocalDateTime startTime, 
            LocalDateTime endTime
    );
}
