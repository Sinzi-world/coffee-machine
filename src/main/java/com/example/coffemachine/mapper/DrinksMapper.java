package com.example.coffemachine.mapper;

import com.example.coffemachine.model.entity.Drinks;
import com.example.coffemachine.model.dto.DrinkDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface DrinksMapper {

    DrinkDTO toDto(Drinks drinks);

    Drinks toEntity(DrinkDTO drinkDTO);
}

