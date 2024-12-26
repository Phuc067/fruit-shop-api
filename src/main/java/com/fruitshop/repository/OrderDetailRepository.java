package com.fruitshop.repository;

import java.util.List;

import com.fruitshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

	List<OrderDetail> findByOrder(Order order);

	List<OrderDetail> findByOrderId(String id);

  boolean existsByProduct(Product product);
}
