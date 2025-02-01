package com.example.coffemachine.service.impl;

import com.example.coffemachine.mapper.DrinksMapper;
import com.example.coffemachine.model.dto.DrinkDTO;
import com.example.coffemachine.model.dto.OrderDTO;
import com.example.coffemachine.model.entity.Ingredients;
import com.example.coffemachine.model.entity.Drinks;
import com.example.coffemachine.model.entity.Orders;
import com.example.coffemachine.repository.IngredientsRepository;
import com.example.coffemachine.repository.DrinksRepository;
import com.example.coffemachine.repository.OrderRepository;
import com.example.coffemachine.service.CoffeeMachineService;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Service
public class CoffeeMachineServiceImpl implements CoffeeMachineService {

    private final DrinksRepository drinksRepository;
    private final IngredientsRepository ingredientsRepository;
    private final OrderRepository orderRepository;
    private final DrinksMapper drinksMapper;

    @Override
    public List<Drinks> getAllDrinks() {
        return drinksRepository.findAll();
    }

    @Override
    public List<Ingredients> getAllIngredients() {
        return ingredientsRepository.findAll();
    }

    @Override
    public Integer getIngredientQuantity(String ingredientName) {
        Ingredients ingredient = ingredientsRepository.findByName(ingredientName);
        if (ingredient != null) {
            return ingredient.getQuantity();
        } else {
            throw new RuntimeException("Ingredient not found: " + ingredientName);
        }
    }

    @Override
    public OrderDTO createOrder(String drinkName) {
        Drinks drink = drinksRepository.findByName(drinkName);
        if (drink == null) {
            throw new RuntimeException("Drink not found: " + drinkName);
        }

        System.out.println("Found drink: " + drink.getId() + ", " + drink.getName());

        Map<String, Integer> ingredientQuantities = getStringIntegerMap(drinkName);

        for (Map.Entry<String, Integer> entry : ingredientQuantities.entrySet()) {
            Ingredients storedIngredient = ingredientsRepository.findByName(entry.getKey());
            if (storedIngredient == null || storedIngredient.getQuantity() < entry.getValue()) {
                throw new RuntimeException("Not enough ingredient: " + entry.getKey());
            }
        }

        for (Map.Entry<String, Integer> entry : ingredientQuantities.entrySet()) {
            Ingredients storedIngredient = ingredientsRepository.findByName(entry.getKey());
            storedIngredient.setQuantity(storedIngredient.getQuantity() - entry.getValue());
            ingredientsRepository.save(storedIngredient); // Сохраняем обновленные данные ингредиента в БД
        }

        Orders order = new Orders();
        order.setDrink(drink);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        DrinkDTO drinkDTO = drinksMapper.toDto(drink);

        return OrderDTO.builder()
                .id(order.getId())
                .drink(drinkDTO)
                .createdAt(order.getCreatedAt())
                .build();
    }

    private static Map<String, Integer> getStringIntegerMap(String drinkName) {
        Map<String, Integer> ingredientQuantities = new HashMap<>();
        if (drinkName.equalsIgnoreCase("Espresso")) {
            ingredientQuantities.put("coffee", 7);
            ingredientQuantities.put("water", 30);
        } else if (drinkName.equalsIgnoreCase("Americano")) {
            ingredientQuantities.put("coffee", 5);
            ingredientQuantities.put("water", 130);
        } else if (drinkName.equalsIgnoreCase("Cappuccino")) {
            ingredientQuantities.put("coffee", 20);
            ingredientQuantities.put("milk", 100);
        } else {
            throw new RuntimeException("Drink not supported: " + drinkName);
        }
        return ingredientQuantities;
    }

    @Override
    public void addIngredient(String ingredientName, Integer quantity) {
        Ingredients ingredient = ingredientsRepository.findByName(ingredientName);
        if (ingredient == null) {
            throw new RuntimeException("Ingredient not found: " + ingredientName);
        }
        ingredient.setQuantity(ingredient.getQuantity() + quantity);
        ingredientsRepository.save(ingredient);
    }
}
