package com.farmstory.repository;

import com.farmstory.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o join fetch o.orderItems WHERE o.orderNo = :id")
    Optional<Order> findByIdWithPrice(@Param("id") int id);
}
