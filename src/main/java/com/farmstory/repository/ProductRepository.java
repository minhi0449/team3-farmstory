package com.farmstory.repository;

import com.farmstory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    public Product findById(int prodNo);
    long count();
    List<Product> findByType(String type);
    Page<Product> findByType(String type, Pageable pageable);

}