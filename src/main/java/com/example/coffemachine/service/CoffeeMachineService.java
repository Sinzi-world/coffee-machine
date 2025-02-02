package com.example.coffemachine.service;

import com.example.coffemachine.model.dto.CustomDrinkDTO;
import com.example.coffemachine.model.dto.DrinkDTO;
import com.example.coffemachine.model.dto.IngredientDTO;
import com.example.coffemachine.model.dto.OrderDTO;
import java.util.List;

public interface CoffeeMachineService {


    List<DrinkDTO> getAllDrinks();

    DrinkDTO getDrinkById(Long id);

    List<IngredientDTO> getAllIngredients();

    Integer getIngredientQuantity(String ingredientName);

    OrderDTO createOrder(String drinkName);

    void addIngredient(String ingredientName, Integer quantity);

    void addCustomDrink(CustomDrinkDTO customDrinkDTO);

}

