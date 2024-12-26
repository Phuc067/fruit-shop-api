package com.fruitshop.service;

import java.time.Instant;
import java.util.Optional;

import com.fruitshop.constant.OrderStatus;
import com.fruitshop.dto.request.OrderRequest;
import com.fruitshop.entity.Order;
import com.fruitshop.model.ResponseObject;

public interface OrderService {
	ResponseObject createOrder(OrderRequest request);
	ResponseObject getPageOrder(Integer userId, Optional<Integer> pageNumber, Optional<Integer> among, String state);
	ResponseObject updateStatus(String id);
	ResponseObject cancelOrder(String orderId);
	void UpdateOrderStateAndInsertLog(Order order,OrderStatus orderStatus, Instant time, String performedBy);
}
