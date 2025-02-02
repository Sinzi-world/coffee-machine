package com.example.coffemachine.service.impl;

import com.example.coffemachine.mapper.DrinksMapper;
import com.example.coffemachine.model.dto.CustomDrinkDTO;
import com.example.coffemachine.model.dto.DrinkDTO;
import com.example.coffemachine.model.dto.OrderDTO;
import com.example.coffemachine.model.entity.DrinkIngredients;
import com.example.coffemachine.model.entity.Ingredients;
import com.example.coffemachine.model.entity.Drinks;
import com.example.coffemachine.model.entity.Orders;
import com.example.coffemachine.repository.DrinkIngredientsRepository;
import com.example.coffemachine.repository.IngredientsRepository;
import com.example.coffemachine.repository.DrinksRepository;
import com.example.coffemachine.repository.OrderRepository;
import com.example.coffemachine.service.CoffeeMachineService;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Service
public class CoffeeMachineServiceImpl implements CoffeeMachineService {

    private final DrinksRepository drinksRepository;
    private final IngredientsRepository ingredientsRepository;
    private final OrderRepository orderRepository;
    private final DrinkIngredientsRepository drinkIngredientsRepository;
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

        List<DrinkIngredients> drinkIngredients = drinkIngredientsRepository.findByDrinkId(drink.getId());
        if (drinkIngredients.isEmpty()) {
            throw new RuntimeException("No ingredient composition found for: " + drinkName);
        }

        for (DrinkIngredients drinkIngredients1 : drinkIngredients) {
            Ingredients storedIngredient = ingredientsRepository.findById(drinkIngredients1.getIngredient().getId())
                    .orElseThrow(() -> new RuntimeException("Ingredient not found: "
                            + drinkIngredients1.getIngredient().getName()));

            if (storedIngredient.getQuantity() < drinkIngredients1.getAmount()) {
                throw new RuntimeException("Not enough ingredient: " + storedIngredient.getName());
            }

            storedIngredient.setQuantity(storedIngredient.getQuantity() - drinkIngredients1.getAmount());
            ingredientsRepository.save(storedIngredient);
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

    @Override
    public void addIngredient(String ingredientName, Integer quantity) {
        Ingredients ingredient = ingredientsRepository.findByName(ingredientName);
        if (ingredient == null) {
            throw new RuntimeException("Ingredient not found: " + ingredientName);
        }
        ingredient.setQuantity(ingredient.getQuantity() + quantity);
        ingredientsRepository.save(ingredient);
    }

    @Override
    public void addCustomDrink(CustomDrinkDTO customDrinkDTO) {
        Drinks drink = new Drinks();
        drink.setName(customDrinkDTO.getDrinkName());
        drinksRepository.save(drink);

        for (Map.Entry<String, Integer> entry : customDrinkDTO.getIngredients().entrySet()) {
            Ingredients ingredient = ingredientsRepository.findByName(entry.getKey());
            if (ingredient == null) {
                throw new RuntimeException("Ingredient not found: " + entry.getKey());
            }

            DrinkIngredients drinkIngredients = new DrinkIngredients();
            drinkIngredients.setDrink(drink);
            drinkIngredients.setIngredient(ingredient);
            drinkIngredients.setAmount(entry.getValue());
            drinkIngredientsRepository.save(drinkIngredients);
        }
    }
}
