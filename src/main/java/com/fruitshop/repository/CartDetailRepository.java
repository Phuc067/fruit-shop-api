package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.CartDetail;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {

}
