package com.fruitshop.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @PutMapping("/{id}")
  public  ResponseEntity<ResponseObject> updateProduct(@PathVariable("id")Integer id, @RequestBody ProductRequest request){
    ResponseObject responseObject = productService.updateProduct(id, request);
    return ResponseEntity.ok(responseObject);
  }

  @DeleteMapping("/{id}")
  public  ResponseEntity<ResponseObject> deleteProduct(@PathVariable("id")Integer id){
    ResponseObject responseObject = productService.deleteProduct(id);
    return ResponseEntity.ok(responseObject);
  }
}
