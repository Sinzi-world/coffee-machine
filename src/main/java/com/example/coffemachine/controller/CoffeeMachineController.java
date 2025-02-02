package com.example.coffemachine.controller;

import com.example.coffemachine.mapper.DrinksMapper;
import com.example.coffemachine.mapper.IngredientsMapper;
import com.example.coffemachine.model.dto.CustomDrinkDTO;
import com.example.coffemachine.model.dto.DrinkDTO;
import com.example.coffemachine.model.dto.OrderDTO;
import com.example.coffemachine.model.entity.Ingredients;
import com.example.coffemachine.service.impl.CoffeeMachineServiceImpl;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RestController
@RequestMapping("/api/coffee-machine")
public class CoffeeMachineController {

    private final CoffeeMachineServiceImpl coffeeMachineService;
    private final DrinksMapper drinksMapper;
    private final IngredientsMapper ingredientsMapper;


    @GetMapping("/drinks")
    public List<DrinkDTO> getAllDrinks() {
        return coffeeMachineService.getAllDrinks().stream()
                .map(drinksMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/drinks/{id}")
    public DrinkDTO getDrinkById(@PathVariable Long id) {
        return coffeeMachineService.getAllDrinks().stream()
                .filter(drink -> drink.getId().equals(id))
                .findFirst()
                .map(drinksMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Drink not found with id: " + id));
    }

    @GetMapping("/ingredients")
    public List<Ingredients> getAllIngredients() {
        return coffeeMachineService.getAllIngredients();
    }


    @GetMapping("/ingredients/{name}")
    public Integer getIngredientQuantity(@PathVariable String name) {
        return coffeeMachineService.getIngredientQuantity(name);
    }

    @PostMapping("/orders")
    public OrderDTO createOrder(@RequestParam String drinkName) {
        return coffeeMachineService.createOrder(drinkName);
    }

    @PostMapping("/ingredients")
    public void addIngredient(@RequestParam String ingredientName, @RequestParam Integer quantity) {
        coffeeMachineService.addIngredient(ingredientName, quantity);
    }

    @PostMapping("/drinks")
    public void addCustomDrink(@RequestBody CustomDrinkDTO customDrink) {
        coffeeMachineService.addCustomDrink(customDrink);
    }
}
