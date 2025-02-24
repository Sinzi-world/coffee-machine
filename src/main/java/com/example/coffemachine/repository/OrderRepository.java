package com.example.coffemachine.repository;

import com.example.coffemachine.model.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query(value = "SELECT * FROM orders o ORDER BY o.created_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Orders> findOrdersWithPagination(int offset, int limit);

    long count();
}
