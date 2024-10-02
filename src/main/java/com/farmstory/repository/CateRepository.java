package com.farmstory.repository;

import com.farmstory.entity.Cate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CateRepository extends JpaRepository<Cate, Integer> {

    public Optional<Cate> findByCateGroupAndCateName(String cateGroup, String cateName);

}
