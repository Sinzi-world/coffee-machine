package com.example.coffemachine.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "drink_statistics")
public class DrinkStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "drink_name")
    private String drinkName;

    @Column(name = "order_count")
    private Integer orderCount = 0;


    @OneToOne
    @JoinColumn(name = "drink_id", nullable = false, unique = true)
    private Drinks drink;
}