package com.example.coffemachine.service.impl;

import com.example.coffemachine.mapper.DrinksMapper;
import com.example.coffemachine.model.dto.CustomDrinkDTO;
import com.example.coffemachine.model.dto.DrinkDTO;
import com.example.coffemachine.model.dto.IngredientDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Service
@Slf4j
public class CoffeeMachineServiceImpl implements CoffeeMachineService {

    private final DrinksRepository drinksRepository;
    private final IngredientsRepository ingredientsRepository;
    private final OrderRepository orderRepository;
    private final DrinkIngredientsRepository drinkIngredientsRepository;
    private final DrinksMapper drinksMapper;

    @Override
    public List<DrinkDTO> getAllDrinks() {
        log.info("Fetching all drinks");
        List<Drinks> drinks = drinksRepository.findAll();
        return drinks.stream()
                .map(drinksMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DrinkDTO getDrinkById(Long id) {
        log.info("Fetching drink by id: {}", id);
        Drinks drink = drinksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drink not found with id: " + id));
        return drinksMapper.toDto(drink);
    }

    @Override
    public List<IngredientDTO> getAllIngredients() {
        log.info("Fetching all ingredients");
        List<Ingredients> ingredients = ingredientsRepository.findAll();
        return ingredients.stream()
                .map(ingredient -> new IngredientDTO(ingredient.getId(), ingredient.getName()))
                .collect(Collectors.toList());
    }


    @Override
    public Integer getIngredientQuantity(String ingredientName) {
        Ingredients ingredient = ingredientsRepository.findByName(ingredientName);
        if (ingredient != null) {
            log.info("Ingredient {} found with quantity: {}",
                    ingredientName, ingredient.getQuantity());
            return ingredient.getQuantity();
        } else {
            log.error("Ingredient not found: {}", ingredientName);
            throw new RuntimeException("Ingredient not found: " + ingredientName);
        }
    }

    @Override
    public OrderDTO createOrder(String drinkName) {
        log.info("Creating order for drink: {}", drinkName);

        Drinks drink = drinksRepository.findByName(drinkName);
        if (drink == null) {
            log.error("Drink not found: {}", drinkName);
            throw new RuntimeException("Drink not found: " + drinkName);
        }

        List<DrinkIngredients> drinkIngredients = drinkIngredientsRepository.findByDrinkId(drink.getId());
        if (drinkIngredients.isEmpty()) {
            log.error("No ingredient composition found for: {}", drinkName);
            throw new RuntimeException("No ingredient composition found for: " + drinkName);
        }

        for (DrinkIngredients drinkIngredients1 : drinkIngredients) {
            Ingredients storedIngredient = ingredientsRepository
                    .findById(drinkIngredients1.getIngredient().getId())
                    .orElseThrow(() -> new RuntimeException("Ingredient not found: "
                            + drinkIngredients1.getIngredient().getName()));

            if (storedIngredient.getQuantity() < drinkIngredients1.getAmount()) {
                log.error("Not enough ingredient: {} (Available: {}, Required: {})",
                        storedIngredient.getName(), storedIngredient.getQuantity(),
                        drinkIngredients1.getAmount());

                throw new RuntimeException("Not enough ingredient: " + storedIngredient.getName());
            }

            storedIngredient.setQuantity(storedIngredient.getQuantity() - drinkIngredients1.getAmount());
            ingredientsRepository.save(storedIngredient);
            log.info("Updated ingredient quantity for {}: {}",
                    storedIngredient.getName(), storedIngredient.getQuantity());
        }

        Orders order = new Orders();
        order.setDrink(drink);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        DrinkDTO drinkDTO = drinksMapper.toDto(drink);

        log.info("Order created successfully with ID: {}", order.getId());

        return OrderDTO.builder()
                .id(order.getId())
                .drink(drinkDTO)
                .createdAt(order.getCreatedAt())
                .build();
    }

    @Override
    public void addIngredient(String ingredientName, Integer quantity) {
        log.info("Adding {} of ingredient: {}", quantity, ingredientName);

        Ingredients ingredient = ingredientsRepository.findByName(ingredientName);
        if (ingredient == null) {
            log.error("Ingredient not found: {}", ingredientName);
            throw new RuntimeException("Ingredient not found: " + ingredientName);
        }
        ingredient.setQuantity(ingredient.getQuantity() + quantity);
        ingredientsRepository.save(ingredient);
        log.info("Updated quantity for ingredient {}: {}", ingredientName, ingredient.getQuantity());
    }

    @Override
    public void addCustomDrink(CustomDrinkDTO customDrinkDTO) {
        log.info("Adding custom drink: {}", customDrinkDTO.getDrinkName());

        Drinks drink = new Drinks();
        drink.setName(customDrinkDTO.getDrinkName());
        drinksRepository.save(drink);

        for (Map.Entry<String, Integer> entry : customDrinkDTO.getIngredients().entrySet()) {
            Ingredients ingredient = ingredientsRepository.findByName(entry.getKey());
            if (ingredient == null) {
                log.error("Ingredient not found: {}", entry.getKey());
                throw new RuntimeException("Ingredient not found: " + entry.getKey());
            }

            DrinkIngredients drinkIngredients = new DrinkIngredients();
            drinkIngredients.setDrink(drink);
            drinkIngredients.setIngredient(ingredient);
            drinkIngredients.setAmount(entry.getValue());
            drinkIngredientsRepository.save(drinkIngredients);
            log.info("Added ingredient: {} with amount: {}", entry.getKey(), entry.getValue());
        }

        log.info("Custom drink {} added successfully", customDrinkDTO.getDrinkName());
    }
}
