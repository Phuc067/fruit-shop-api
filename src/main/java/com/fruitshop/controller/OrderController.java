package com.fruitshop.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.dto.request.OrderRequest;
import com.fruitshop.entity.Order;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.OrderService;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping(ApiPath.ORDER)
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("")
	ResponseEntity<ResponseObject> createOrder(@RequestBody OrderRequest request) {
		ResponseObject responseObject = orderService.createOrder(request);
		return ResponseEntity.ok(responseObject);
	}

	@GetMapping("")
	ResponseEntity<ResponseObject> getOrderByState(@RequestParam( value = "userId", required = false, defaultValue = "") Integer userId,
			@RequestParam(value = "state", required = false, defaultValue = "") String state,
			@RequestParam("page") Optional<Integer> pageNumber, @RequestParam("amount") Optional<Integer> amount) {
		ResponseObject responseObject = orderService.getPageOrder(userId, pageNumber, amount, state);
		return ResponseEntity.ok(responseObject);
	}

	@PutMapping("/{orderId}")
	ResponseEntity<ResponseObject> updateOrderStatus(@PathVariable("orderId") String id) {
		ResponseObject responseObject = orderService.updateStatus(id);
		return ResponseEntity.ok(responseObject);
	}

	@PutMapping("/{orderId}/cancel")
	ResponseEntity<ResponseObject> cancelOrder(@PathVariable("orderId") String orderId) {
		ResponseObject responseObject = orderService.cancelOrder(orderId);
		return ResponseEntity.ok(responseObject);
	}
}
