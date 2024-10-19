package com.fruitshop.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fruitshop.dto.request.CartRequest;
import com.fruitshop.entity.CartDetail;
import com.fruitshop.mapper.CartMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.CartDetailRepository;
import com.fruitshop.service.CartService;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private CartDetailRepository cartDetailRepository;
	
	@Override
	public ResponseObject addToCart(CartRequest cartRequest) {
		
		CartDetail cartDetail = cartDetailRepository.findByUserIdAndProductId(cartRequest.getProductId(), cartRequest.getUserId());
		if(ObjectUtils.isEmpty(cartDetail))
		{
			CartDetail newCartDetail = CartMapper.INSTANT.toCartDetail(cartRequest);
			cartDetailRepository.save(newCartDetail);
		}
		else {
			int oldQuantity = cartDetail.getQuantity();
			int newQuantity = oldQuantity + cartRequest.getQuantity();
			cartDetail.setQuantity(newQuantity);
			cartDetailRepository.save(cartDetail);
		}
		
		int totalItem = cartDetailRepository.getCountProductByUserId(cartRequest.getUserId());
		return new ResponseObject(HttpStatus.CREATED, "Thêm sản phẩm vào giỏ hàng thành công", totalItem);
	}

}
