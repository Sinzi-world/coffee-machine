package com.example.coffemachine.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "drink_ingredients")
public class DrinkIngredients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с напитком
    @ManyToOne
    @JoinColumn(name = "drink_id", referencedColumnName = "id", nullable = false)
    private Drinks drink;

    // Связь с ингредиентом
    @ManyToOne
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id", nullable = false)
    private Ingredients ingredient;

    @Column(name = "amount")
    private Integer amount;
}

