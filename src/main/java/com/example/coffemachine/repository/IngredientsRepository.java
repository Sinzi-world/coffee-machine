package com.example.coffemachine.repository;

import com.example.coffemachine.model.entity.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IngredientsRepository extends JpaRepository<Ingredients, Long> {

    Ingredients findByName(String name);

}
