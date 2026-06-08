package com.foodchain.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double quantity;
    private String unit; // kg, packets, etc.
    private LocalDateTime expiryDate;
    private String status; // AVAILABLE, CLAIMED, DONATED
}
