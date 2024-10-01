package com.farmstory.repository;

import com.farmstory.entity.Cart;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{
    List<Cart> findByUserUid(String uid);
    //public Optional<Cart> findByProductProdNo(int;


    Optional<Cart> findByProductProdNo(Integer prodNo);

    //public void deleteAllById(List<Long> data);

    //Boolean existsByProductId(int productId);

    //List<Long> deleteAllById(List<Long> data);

}