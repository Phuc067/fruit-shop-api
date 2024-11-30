package com.fruitshop.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fruitshop.constant.OrderStatus;
import com.fruitshop.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>{
	List<Order> findByUserId(Integer userId);
	List<Order> findByUserIdAndState(Integer userId, OrderStatus state);
	
	@Query("SELECT o FROM Order o WHERE o.state IN :states")
	Page<Order> findByState(@Param("states") List<OrderStatus> states, Pageable pageable);
}
