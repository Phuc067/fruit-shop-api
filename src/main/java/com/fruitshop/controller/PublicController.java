package com.fruitshop.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.CategoryService;
import com.fruitshop.service.ProductService;

@RestController
@RequestMapping(ApiPath.PUBLIC)
public class PublicController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/products")
	public ResponseEntity<ResponseObject> getPageProduct(@RequestParam("page") Optional<Integer> pageNumber,
			@RequestParam("amount") Optional<Integer> amount,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "sortType", required = false, defaultValue = "") String sortType) {
		ResponseObject responseObject = productService.getPageProduct(pageNumber, amount, keyword, sortType);
		return ResponseEntity.ok(responseObject);
	}
	
	@GetMapping("/products/{id}")
	public ResponseEntity<ResponseObject> getProductById(@PathVariable("id") Integer id){
		ResponseObject responseObject = productService.getProductByid(id);
		return ResponseEntity.ok(responseObject);
	}
	
	@GetMapping("/categories")
	public ResponseEntity<ResponseObject> getAllCategory(){
		ResponseObject responseObject = categoryService.getAll();
		return ResponseEntity.ok(responseObject);
	}
	
	

}
