package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {

}
