package com.fruitshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.dto.request.CartRequest;
import com.fruitshop.dto.request.IntegerObject;
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
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getCartByUserId(@PathVariable("id") Integer userId)
	{
		ResponseObject responseObject = cartService.getByUserId(userId);
		return ResponseEntity.ok(responseObject);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseObject> updateProductQuantity(@PathVariable("id") Integer id, @RequestBody IntegerObject object)
	{
		ResponseObject responseObject = cartService.updateProductQuantity(id,object);
		return ResponseEntity.ok(responseObject);
	}
	
	@DeleteMapping("/multiple")
	public ResponseEntity<ResponseObject> deleteProductsFromCart(@RequestBody List<Integer> ids) {
	    ResponseObject responseObject = cartService.deleteCartDetails(ids);
	    return ResponseEntity.ok(responseObject);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseObject> deleteProductFromCart(@PathVariable("id") Integer id)
	{
		ResponseObject responseObject = cartService.deleteCartDetail(id);
		return ResponseEntity.ok(responseObject);
	}
	
	

}
