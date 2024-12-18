package com.fruitshop.service;

import java.util.Optional;

import com.fruitshop.dto.request.ProductRequest;
import com.fruitshop.dto.response.ProductDiscount;
import com.fruitshop.dto.response.ProductResponse;
import com.fruitshop.entity.Product;
import com.fruitshop.model.ResponseObject;

public interface ProductService {
	ResponseObject getAll();

	ResponseObject createProduct(ProductRequest request);

  ResponseObject updateProduct(Integer id, ProductRequest request);

	ResponseObject getPageProduct(Optional<Integer> pageNumber, Optional<Integer> amount, String keyword, String sortType);

	ResponseObject getProductByid(Integer id);

  ProductDiscount getProductDiscount(Integer productId);
}
