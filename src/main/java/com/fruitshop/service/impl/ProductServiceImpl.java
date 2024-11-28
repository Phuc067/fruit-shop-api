package com.fruitshop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fruitshop.dto.request.ProductRequest;
import com.fruitshop.dto.response.ProductResponse;
import com.fruitshop.entity.Category;
import com.fruitshop.entity.Product;
import com.fruitshop.mapper.ProductMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.CategoryRepository;
import com.fruitshop.repository.ProductRepository;
import com.fruitshop.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public ResponseObject getAll() {
		List<Product> products = productRepository.findAll();
		List<ProductResponse> productResponses = new ArrayList<ProductResponse>();
		for (Product product : products) {
			ProductResponse productResponse = ProductMapper.INSTANT.entityToResponse(product);
			int discountPercentage = productRepository.getProductDiscount(product.getId());
			productResponse.setDiscountPercentage(discountPercentage);
			productResponses.add(productResponse);
		}
		return new ResponseObject(HttpStatus.ACCEPTED, "Lấy danh sách sản phẩm thành công", productResponses);
	}

	@Override
	public ResponseObject createProduct(ProductRequest request) {
		ResponseObject validationResponse = validate(request);
		if (validationResponse != null) {
			return validationResponse;
		}

		
		Product product = ProductMapper.INSTANT.requestToEnity(request);
		productRepository.save(product);

		ProductResponse productResponse = ProductMapper.INSTANT.entityToResponse(product);
		return new ResponseObject(HttpStatus.ACCEPTED, "Thêm sản phẩm thành công", productResponse);
	}

	private ResponseObject validate(ProductRequest request) {
		if (request.getPrice() < 0)
			return new ResponseObject(HttpStatus.BAD_REQUEST, "Giá sản phẩm không hợp lệ", null);
		Boolean existsByTitle = productRepository.existsByTitle(request.getTitle());
		
		if (existsByTitle)
			return new ResponseObject(HttpStatus.BAD_REQUEST, "Tên sản phẩm bị trùng", null);

		Optional<Category> categoryDB = categoryRepository.findById(request.getCategory());
		if (categoryDB.isEmpty())
			return new ResponseObject(HttpStatus.NOT_FOUND, "không tìm thấy danh mục sản phẩm", null);

		return null;
	}

}
