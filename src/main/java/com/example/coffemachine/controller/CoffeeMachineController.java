package com.example.coffemachine.controller;

import com.example.coffemachine.mapper.DrinksMapper;
import com.example.coffemachine.mapper.IngredientsMapper;
import com.example.coffemachine.model.dto.CustomDrinkDTO;
import com.example.coffemachine.model.dto.DrinkDTO;
import com.example.coffemachine.model.dto.IngredientDTO;
import com.example.coffemachine.model.dto.OrderDTO;
import com.example.coffemachine.service.impl.CoffeeMachineServiceImpl;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Data
@RestController
@RequestMapping("/api/coffee-machine")
public class CoffeeMachineController {

    private final CoffeeMachineServiceImpl coffeeMachineService;
    private final DrinksMapper drinksMapper;
    private final IngredientsMapper ingredientsMapper;


    @GetMapping("/drinks")
    public List<DrinkDTO> getAllDrinks() {
        return coffeeMachineService.getAllDrinks();
    }

    @GetMapping("/drinks/{id}")
    public DrinkDTO getDrinkById(@PathVariable Long id) {
        return coffeeMachineService.getDrinkById(id);
    }

    @GetMapping("/ingredients")
    public List<IngredientDTO> getAllIngredients() {
        return coffeeMachineService.getAllIngredients();
    }

    @GetMapping("/ingredients/{name}")
    public Integer getIngredientQuantity(@PathVariable String name) {
        return coffeeMachineService.getIngredientQuantity(name);
    }

    @GetMapping("/history")
    public List<OrderDTO> getOrders(
            @RequestParam int offset,
            @RequestParam int limit) {
        System.out.println("Offset: " + offset + ", Limit: " + limit);
        return coffeeMachineService.getOrders(offset, limit);
    }

    @GetMapping("/history/count")
    public Integer getHistoryCount() {
        return coffeeMachineService.getTotalOrdersCount();
    }

    @GetMapping("/drinks/{id}/ingredients")
    public List<IngredientDTO> getDrinkIngredientsById(@PathVariable Long id) {
        return coffeeMachineService.getDrinkIngredientsById(id);
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
