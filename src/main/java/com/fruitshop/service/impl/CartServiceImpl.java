package com.fruitshop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fruitshop.dto.request.CartRequest;
import com.fruitshop.dto.request.IntegerObject;
import com.fruitshop.dto.request.ListInteger;
import com.fruitshop.dto.response.CartResponse;
import com.fruitshop.entity.CartDetail;
import com.fruitshop.entity.Product;
import com.fruitshop.mapper.CartMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.CartDetailRepository;
import com.fruitshop.repository.ProductRepository;
import com.fruitshop.service.CartService;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private CartDetailRepository cartDetailRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
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

	@Override
	public ResponseObject getByUserId(int userId) {
		List<CartDetail> cartDetails =  cartDetailRepository.findByUserId(userId);
		List<CartResponse> cartResponses = new ArrayList<CartResponse>();
		for(CartDetail cartDetail: cartDetails) {
			CartResponse cartResponse = CartMapper.INSTANT.toCartResponse(cartDetail);
			int discountPercentage = productRepository.getProductDiscount(cartDetail.getProduct().getId());
			cartResponse.getProduct().setDiscountPercentage(discountPercentage);
			cartResponses.add(cartResponse);
		}
		return new ResponseObject(HttpStatus.OK, "Lấy thông tin giỏ hàng thành công", cartResponses);
	}

	@Override
	public ResponseObject updateProductQuantity(Integer id , IntegerObject object) {
		if(ObjectUtils.isEmpty(object)) return new  ResponseObject(HttpStatus.BAD_REQUEST, "Số lượng không hợp lệ", null);
		int quantity = object.getValue();
		
		Optional<CartDetail> cartDetailDB = cartDetailRepository.findById(id);
		if(ObjectUtils.isEmpty(cartDetailDB)) return new  ResponseObject(HttpStatus.BAD_REQUEST, "Sản phẩm không tồn tại trong giỏ hàng", null);
		
		CartDetail cartDetail = cartDetailDB.get();
		if( quantity <= 0 || quantity > cartDetail.getProduct().getQuantity()) return new ResponseObject(HttpStatus.BAD_REQUEST,"Số lượng còn lại của sản phẩm không đủ", null);

		cartDetail.setQuantity(quantity);
		cartDetailRepository.save(cartDetail);
		return new  ResponseObject(HttpStatus.ACCEPTED, "Cập nhật thành công", cartDetail);
	}

	@Override
	public ResponseObject deleteCartDetail(Integer id) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		Object object = authentication.getName();
//		System.out.println(object);
		Optional<CartDetail> cartDetail = cartDetailRepository.findById(id);
		if(cartDetail.isEmpty()) return new ResponseObject(HttpStatus.NOT_FOUND, "Không tìm thấy chi tiết giỏ hàng", null);
		cartDetailRepository.delete(cartDetail.get());
		return new ResponseObject(HttpStatus.ACCEPTED, "Xóa thành công", id);
		
	}

	@Override
	@Transactional
	public ResponseObject deleteCartDetails(List<Integer> listId) {
		cartDetailRepository.deleteCartItemsByIds(listId);
		return new ResponseObject(HttpStatus.ACCEPTED, "Xóa thành công", listId);
	}

}
