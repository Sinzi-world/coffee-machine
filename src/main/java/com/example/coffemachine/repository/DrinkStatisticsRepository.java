package com.example.coffemachine.repository;

import com.example.coffemachine.model.entity.DrinkStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DrinkStatisticsRepository extends JpaRepository<DrinkStatistics, Long> {
    Optional<DrinkStatistics> findByDrinkName(String drinkName);

    Optional<DrinkStatistics> findTopByOrderByOrderCountDesc();
}

