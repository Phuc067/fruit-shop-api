package com.fruitshop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.dto.request.OrderRequest;
import com.fruitshop.entity.Order;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.OrderService;

@RestController
@RequestMapping(ApiPath.ORDER)
public class OrderController {
	
	@Autowired
	private OrderService orderService; 
	
	@PostMapping("")
	ResponseEntity<ResponseObject> createOrder(@RequestBody OrderRequest request){
		ResponseObject responseObject = orderService.createOrder(request);
		return ResponseEntity.ok(responseObject);
	}
}
