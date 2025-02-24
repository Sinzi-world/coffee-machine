package com.example.coffemachine.repository;

import com.example.coffemachine.model.entity.Drinks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinksRepository extends JpaRepository<Drinks, Long> {

    Drinks findByName(String name);
}
