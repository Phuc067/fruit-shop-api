package com.fruitshop.service;

import com.fruitshop.dto.request.CartRequest;
import com.fruitshop.model.ResponseObject;

public interface CartService {
	ResponseObject addToCart(CartRequest cartRequest);
}
