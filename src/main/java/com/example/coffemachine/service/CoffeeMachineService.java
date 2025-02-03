package com.example.coffemachine.service;

import com.example.coffemachine.model.dto.*;

import java.util.List;

public interface CoffeeMachineService {


    List<DrinkDTO> getAllDrinks();

    DrinkDTO getDrinkById(Long id);

    List<IngredientDTO> getAllIngredients();

    Integer getIngredientQuantity(String ingredientName);

    OrderDTO createOrder(String drinkName);

    DrinkStatisticsDTO getDrinkStatistics(String drinkName);

    DrinkStatisticsDTO getMostPopularDrink();

    void addIngredient(String ingredientName, Integer quantity);

    void addCustomDrink(CustomDrinkDTO customDrinkDTO);

}

