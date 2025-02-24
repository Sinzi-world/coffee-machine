package com.example.coffemachine.mapper;

import com.example.coffemachine.model.entity.Drinks;
import com.example.coffemachine.model.dto.DrinkDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface DrinksMapper {

    @Mapping(target = "id", source = "drinks.id")
    @Mapping(target = "name", source = "drinks.name")
    DrinkDTO toDto(Drinks drinks);

    Drinks toEntity(DrinkDTO drinkDTO);
}


