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
	Page<Order> findByUserId(Integer userId, Pageable pageable);
	
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.state IN :states")
	Page<Order> findByUserIdAndState(@Param("userId") Integer userId, @Param("states") List<OrderStatus> states, Pageable pageable);
	
	@Query("SELECT o FROM Order o WHERE o.state IN :states")
	Page<Order> findByState(@Param("states") List<OrderStatus> states, Pageable pageable);
}
