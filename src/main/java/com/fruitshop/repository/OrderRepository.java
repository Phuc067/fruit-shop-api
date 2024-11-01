package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

}
