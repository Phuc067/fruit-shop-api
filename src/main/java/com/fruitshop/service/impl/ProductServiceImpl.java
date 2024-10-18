package com.fruitshop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fruitshop.dto.response.ProductResponse;
import com.fruitshop.entity.Product;
import com.fruitshop.mapper.ProductMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.ProductRepository;
import com.fruitshop.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;
	

	
	@Override
	public ResponseObject getAll() {
		List<Product> products =  productRepository.findAll();
		List<ProductResponse> productResponses = new ArrayList<ProductResponse>();
		for(Product product: products) {
			ProductResponse productResponse = ProductMapper.INSTANT.entityToResponse(product);
			int discountPercentage = productRepository.getProductDiscount(product.getId());
			productResponse.setDiscountPercentage(discountPercentage);
			productResponses.add(productResponse);
		}
		return new ResponseObject(HttpStatus.ACCEPTED, "Lấy danh sách sản phẩm thành công", productResponses);
	}
	
}
