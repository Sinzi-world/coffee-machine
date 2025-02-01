package com.example.coffemachine.repository;

import com.example.coffemachine.model.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
