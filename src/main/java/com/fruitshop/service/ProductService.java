package com.fruitshop.service;

import java.util.List;

import com.fruitshop.dto.response.ProductResponse;
import com.fruitshop.entity.Product;
import com.fruitshop.model.ResponseObject;

public interface ProductService {
	ResponseObject getAll();
}
