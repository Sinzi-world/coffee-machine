package com.example.coffemachine.mapper;

import com.example.coffemachine.model.entity.Ingredients;
import com.example.coffemachine.model.dto.IngredientDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientsMapper {


    IngredientDTO toDto(Ingredients ingredients);

    Ingredients toEntity(IngredientDTO ingredientDTO);
}

