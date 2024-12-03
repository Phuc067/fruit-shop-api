package com.fruitshop.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.dto.request.ProductRequest;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.ProductService;

import jakarta.annotation.security.RolesAllowed;


@RestController
@RequestMapping(ApiPath.PRODUCT)
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	
	@GetMapping("")
	public ResponseEntity<ResponseObject> getAllProduct(){
		ResponseObject responseObject = productService.getAll();
		return ResponseEntity.ok(responseObject); 
	}
	
	@PostMapping("")
	public ResponseEntity<ResponseObject> createProduct(@RequestBody ProductRequest request){
		ResponseObject responseObject = productService.createProduct(request);
		return ResponseEntity.ok(responseObject); 
	}
}
