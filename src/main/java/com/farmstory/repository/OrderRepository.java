package com.farmstory.repository;

import com.farmstory.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o join fetch o.orderItems WHERE o.orderNo = :id")
    Optional<Order> findByIdWithPrice(@Param("id") int id);

    @Query("SELECT o FROM Order o WHERE o.user.uid = :uid")
    Page<Order> findAllByUid(@Param("uid") String uid, Pageable pageable);
}
