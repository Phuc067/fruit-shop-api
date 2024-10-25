package com.fruitshop.service;

import java.util.List;

import com.fruitshop.dto.request.CartRequest;
import com.fruitshop.dto.request.IntegerObject;
import com.fruitshop.model.ResponseObject;

public interface CartService {
	ResponseObject addToCart(CartRequest cartRequest);

	ResponseObject getByUserId(int userId);

	ResponseObject updateProductQuantity(Integer id, IntegerObject quantity);

	ResponseObject deleteCartDetail(Integer id);

	ResponseObject deleteCartDetails(List<Integer> listId);
}
