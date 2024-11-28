package com.fruitshop.service;

import com.fruitshop.dto.request.ProductRequest;
import com.fruitshop.model.ResponseObject;

public interface ProductService {
	ResponseObject getAll();

	ResponseObject createProduct(ProductRequest request);
}
