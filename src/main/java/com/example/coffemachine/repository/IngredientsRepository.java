package com.example.coffemachine.repository;

import com.example.coffemachine.model.entity.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface IngredientsRepository extends JpaRepository<Ingredients, Long> {

    Ingredients findByName(String name);

    @Query("SELECT di.ingredient FROM DrinkIngredients di WHERE di.drink.id = :drinkId")
    List<Ingredients> findIngredientsByDrinkId(Long drinkId);
}
