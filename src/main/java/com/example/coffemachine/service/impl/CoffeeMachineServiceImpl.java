package com.example.coffemachine.service.impl;

import com.example.coffemachine.mapper.DrinksMapper;
import com.example.coffemachine.model.dto.*;
import com.example.coffemachine.model.entity.*;
import com.example.coffemachine.repository.*;
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
    private final DrinkStatisticsRepository drinkStatisticsRepository;

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
    public DrinkStatisticsDTO getDrinkStatistics(String drinkName) {
        DrinkStatistics stats = drinkStatisticsRepository.findByDrinkName(drinkName)
                .orElseThrow(() -> new RuntimeException("Statistics not found for drink: " + drinkName));
        return new DrinkStatisticsDTO(stats.getDrinkName(), stats.getOrderCount());
    }

    @Override
    public DrinkStatisticsDTO getMostPopularDrink() {
        DrinkStatistics stats = drinkStatisticsRepository.findTopByOrderByOrderCountDesc()
                .orElseThrow(() -> new RuntimeException("No statistics available"));
        return new DrinkStatisticsDTO(stats.getDrinkName(), stats.getOrderCount());
    }

    @Override
    public List<OrderDTO> getOrders(int offset, int limit) {
        return orderRepository.findOrdersWithPagination(offset, limit)
                .stream()
                .map(order -> OrderDTO.builder()
                        .id(order.getId())
                        .drink(DrinkDTO.builder()
                                .id(order.getDrink().getId())
                                .name(order.getDrink().getName())
                                .build())
                        .createdAt(order.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    public Integer getTotalOrdersCount() {
        return Math.toIntExact(orderRepository.count());
    }


    @Override
    public List<IngredientDTO> getDrinkIngredientsById(Long id) {
        log.info("Fetching ingredients for drink with id: {}", id);
        List<Ingredients> ingredients = ingredientsRepository.findIngredientsByDrinkId(id);
        if (ingredients.isEmpty()) {
            log.error("No ingredients found for drink with id: {}", id);
            throw new RuntimeException("No ingredients found for drink with id: " + id);
        }

        return ingredients.stream()
                .map(ingredient -> new IngredientDTO(ingredient.getId(), ingredient.getName()))
                .collect(Collectors.toList());
    }


    @Override
    public OrderDTO createOrder(String drinkName) {
        log.info("Creating order for drink: {}", drinkName);
        Drinks drink = drinksRepository.findByName(drinkName);
        if (drink == null) {
            log.error("Drink not found: {}", drinkName);
            return OrderDTO.builder()
                    .message("Drink not found: " + drinkName)
                    .build();
        }
        List<DrinkIngredients> drinkIngredients = drinkIngredientsRepository.findByDrinkId(drink.getId());
        if (drinkIngredients.isEmpty()) {
            log.error("No ingredient composition found for: {}", drinkName);
            return OrderDTO.builder()
                    .message("No ingredient composition found for: " + drinkName)
                    .build();
        }

        for (DrinkIngredients drinkIngredient : drinkIngredients) {
            Ingredients storedIngredient = ingredientsRepository
                    .findById(drinkIngredient.getIngredient().getId())
                    .orElse(null);
            if (storedIngredient == null) {
                return OrderDTO.builder()
                        .message("Ingredient not found: " + drinkIngredient.getIngredient().getName())
                        .build();
            }
            if (storedIngredient.getQuantity() < drinkIngredient.getAmount()) {
                log.error("Not enough ingredient: {} (Available: {}, Required: {})",
                        storedIngredient.getName(), storedIngredient.getQuantity(),
                        drinkIngredient.getAmount());
                return OrderDTO.builder()
                        .message("Недостаточно " + storedIngredient.getName() +
                                " (Есть: " + storedIngredient.getQuantity() +
                                ", необходимо: " + drinkIngredient.getAmount() + ")")
                        .build();
            }
            storedIngredient.setQuantity(storedIngredient.getQuantity() - drinkIngredient.getAmount());
            ingredientsRepository.save(storedIngredient);
            log.info("Updated ingredient quantity for {}: {}",
                    storedIngredient.getName(), storedIngredient.getQuantity());
        }

        Orders order = new Orders();
        order.setDrink(drink);
        order.setCreatedAt(LocalDateTime.now());
        Orders savedOrder = orderRepository.save(order);
        DrinkStatistics drinkStatistics = drinkStatisticsRepository.findByDrinkName(drinkName)
                .orElseGet(() -> {
                    DrinkStatistics newStats = new DrinkStatistics();
                    newStats.setDrink(drink);
                    newStats.setDrinkName(drink.getName());
                    newStats.setOrderCount(0);
                    return newStats;
                });

        drinkStatistics.setOrderCount(drinkStatistics.getOrderCount() + 1);
        drinkStatisticsRepository.save(drinkStatistics);
        DrinkDTO drinkDTO = drinksMapper.toDto(drink);
        log.info("Напиток: {} готовится!" , savedOrder.getDrink().getName());

        return OrderDTO.builder()
                .id(savedOrder.getId())
                .drink(drinkDTO)
                .createdAt(savedOrder.getCreatedAt())
                .message("Напиток " + savedOrder.getDrink().getName() + " готовится!")
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
