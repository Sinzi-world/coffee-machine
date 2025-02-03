package com.example.coffemachine.controller;

import com.example.coffemachine.model.dto.DrinkStatisticsDTO;
import com.example.coffemachine.service.impl.CoffeeMachineServiceImpl;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final CoffeeMachineServiceImpl coffeeMachineService;


    @GetMapping("/drink")
    public DrinkStatisticsDTO getDrinkStatistics(@RequestParam String drinkName) {
        return coffeeMachineService.getDrinkStatistics(drinkName);
    }

    @GetMapping("/most-popular")
    public DrinkStatisticsDTO getMostPopularDrink() {
        return coffeeMachineService.getMostPopularDrink();
    }
}
