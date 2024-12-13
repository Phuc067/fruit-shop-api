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
	
	@Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
	Page<Order> findByUserId(Integer userId, Pageable pageable);

	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.state IN :states ORDER BY o.orderDate DESC")
	Page<Order> findByUserIdAndState(@Param("userId") Integer userId, @Param("states") List<OrderStatus> states, Pageable pageable);

	@Query("SELECT o FROM Order o WHERE o.state IN :states ORDER BY o.orderDate DESC")
	Page<Order> findByState(@Param("states") List<OrderStatus> states, Pageable pageable);
	
	@Query("SELECT o FROM Order o WHERE FUNCTION('MONTH', o.orderDate) = :month AND FUNCTION('YEAR', o.orderDate) = :year")
    List<Order> findOrdersByMonthAndYear(@Param("month") int month, @Param("year") int year);
	
}
