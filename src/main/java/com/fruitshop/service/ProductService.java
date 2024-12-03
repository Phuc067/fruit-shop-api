package com.fruitshop.service;

import java.util.Optional;

import com.fruitshop.dto.request.ProductRequest;
import com.fruitshop.model.ResponseObject;

public interface ProductService {
	ResponseObject getAll();

	ResponseObject createProduct(ProductRequest request);

	ResponseObject getPageProduct(Optional<Integer> pageNumber, Optional<Integer> amount, String keyword, String sortType);

}
