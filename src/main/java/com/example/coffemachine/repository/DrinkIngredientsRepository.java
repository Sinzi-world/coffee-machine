package com.example.coffemachine.repository;

import com.example.coffemachine.model.entity.DrinkIngredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkIngredientsRepository extends JpaRepository<DrinkIngredients, Integer> {
    List<DrinkIngredients> findByDrinkId(Long drink_id);
}

