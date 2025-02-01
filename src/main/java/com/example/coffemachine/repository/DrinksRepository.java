package com.example.coffemachine.repository;

import com.example.coffemachine.model.entity.Drinks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DrinksRepository extends JpaRepository<Drinks, Long> {

    @Query(value = "SELECT * FROM drinks WHERE name = :name", nativeQuery = true)
    Drinks findByName(String name);
}
