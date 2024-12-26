package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fruitshop.constant.OrderStatus;
import com.fruitshop.entity.OrderLog;

@Repository
public interface OrderLogRepository extends JpaRepository<OrderLog, Integer> {

  OrderLog findByState(OrderStatus orderStatus);

  @Query("SELECT ol FROM OrderLog ol WHERE ol.order.id = :orderId AND ol.state = :state ORDER BY ol.time DESC")
  OrderLog findByOrderIdAndState(@Param("orderId") String orderId, @Param("state") OrderStatus state);

}
