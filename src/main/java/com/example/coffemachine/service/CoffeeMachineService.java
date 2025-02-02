package com.example.coffemachine.service;

import com.example.coffemachine.model.dto.CustomDrinkDTO;
import com.example.coffemachine.model.dto.OrderDTO;
import com.example.coffemachine.model.entity.Ingredients;
import com.example.coffemachine.model.entity.Drinks;
import java.util.List;

public interface CoffeeMachineService {


    List<Drinks> getAllDrinks();

    List<Ingredients> getAllIngredients();

    Integer getIngredientQuantity(String ingredientName);

    OrderDTO createOrder(String drinkName);

    void addIngredient(String ingredientName, Integer quantity);

    void addCustomDrink(CustomDrinkDTO customDrinkDTO);

}

