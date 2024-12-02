package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.constant.OrderStatus;
import com.fruitshop.entity.OrderLog;

@Repository
public interface OrderLogRepository extends JpaRepository<OrderLog, Integer> {

	OrderLog findByState(OrderStatus orderStatus);

	OrderLog findByOrderIdAndState(String orderId, OrderStatus state);
	
}
