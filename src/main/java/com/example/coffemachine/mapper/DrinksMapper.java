package com.example.coffemachine.mapper;

import com.example.coffemachine.model.entity.Drinks;
import com.example.coffemachine.model.dto.DrinkDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DrinksMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    DrinkDTO toDto(Drinks drinks);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    Drinks toEntity(DrinkDTO drinkDTO);
}

