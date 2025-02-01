package com.example.coffemachine.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private DrinkDTO drink;
    private LocalDateTime createdAt;
}
