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

    public FoodItem updateStatus(Long id, String status) {
    FoodItem item = foodRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Item not found"));
    item.setStatus(status);
    return foodRepository.save(item);
    }
    
    public List<FoodItem> getUrgentDonations() {
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime urgentThreshold = now.plusHours(12);
        return foodRepository.findByStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                "AVAILABLE", 
                now, 
                urgentThreshold
        );
    }
}
