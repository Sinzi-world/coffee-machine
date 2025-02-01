package com.example.coffemachine.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrinkDTO {

    private Long id;
    private String name;
    private Set<IngredientDTO> ingredients;
}
