package com.example.coffemachine.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomDrinkDTO {
    private String drinkName;
    private Map<String, Integer> ingredients;
}