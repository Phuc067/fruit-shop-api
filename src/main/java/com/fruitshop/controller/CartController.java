package com.fruitshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.dto.request.CartRequest;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.CartService;

@RestController
@RequestMapping(ApiPath.CART)
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@PostMapping("")
	public ResponseEntity<ResponseObject> addProductToCart(@RequestBody CartRequest cartRequest)
	{
		ResponseObject responseObject = cartService.addToCart(cartRequest);
		return ResponseEntity.ok(responseObject);
	}
	
}
