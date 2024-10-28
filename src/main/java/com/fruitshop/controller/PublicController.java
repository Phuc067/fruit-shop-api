package com.fruitshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.ProductService;

@RestController
@RequestMapping(ApiPath.PUBLIC)
public class PublicController {


	@Autowired
	private ProductService productService;
	
	@GetMapping("/products")
	public ResponseEntity<ResponseObject> getAllProduct(){
		ResponseObject responseObject = productService.getAll();
		return ResponseEntity.ok(responseObject); 
	}
	
}
