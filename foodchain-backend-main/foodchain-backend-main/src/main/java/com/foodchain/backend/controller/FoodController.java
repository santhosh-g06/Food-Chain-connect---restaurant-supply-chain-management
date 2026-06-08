package com.foodchain.backend.controller;

import com.foodchain.backend.model.FoodItem;
import com.foodchain.backend.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@CrossOrigin(origins = "*") // Allows your friend's React app to connect!
public class FoodController {

    @Autowired
    private FoodService foodService;

    // 1. Endpoint for Restaurants to Add Food
    // URL: POST http://localhost:8080/api/food/add
    @PostMapping("/add")
    public ResponseEntity<FoodItem> addFood(@RequestBody FoodItem foodItem) {
        FoodItem savedFood = foodService.addFoodItem(foodItem);
        return ResponseEntity.ok(savedFood);
    }

    // 2. Endpoint for NGOs to see ALL available food
    // URL: GET http://localhost:8080/api/food/available
    @GetMapping("/available")
    public ResponseEntity<List<FoodItem>> getAvailableFood() {
        return ResponseEntity.ok(foodService.getAvailableFood());
    }

    // 3. THE SMART FEATURE: Endpoint for Urgent Donations
    // URL: GET http://localhost:8080/api/food/urgent
    @GetMapping("/urgent")
    public ResponseEntity<List<FoodItem>> getUrgentFood() {
        return ResponseEntity.ok(foodService.getUrgentDonations());
    }
}
