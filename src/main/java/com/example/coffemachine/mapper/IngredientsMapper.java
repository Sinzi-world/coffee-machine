package com.example.coffemachine.mapper;

import com.example.coffemachine.model.entity.Ingredients;
import com.example.coffemachine.model.dto.IngredientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IngredientsMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "quantity", target = "quantity")
    IngredientDTO toDto(Ingredients ingredients);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "quantity", target = "quantity")
    Ingredients toEntity(IngredientDTO ingredientDTO);
}

