package com.foodchain.backend.service;

import com.foodchain.backend.model.FoodItem;
import com.foodchain.backend.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public FoodItem addFoodItem(FoodItem foodItem) {
        return foodRepository.save(foodItem);
    }

    public List<FoodItem> getAvailableFood() {
        return foodRepository.findByStatus("AVAILABLE");
    }

    // THE SMART FEATURE LOGIC
    public List<FoodItem> getUrgentDonations() {
        // 1. Get the exact time right now
        LocalDateTime now = LocalDateTime.now();
        
        // 2. Set the "Urgency Window" (e.g., expiring in the next 12 hours)
        LocalDateTime urgentThreshold = now.plusHours(12);
        
        // 3. Ask Azure for only those specific items, sorted so the most urgent is at the top
        return foodRepository.findByStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                "AVAILABLE", 
                now, 
                urgentThreshold
        );
    }
}
