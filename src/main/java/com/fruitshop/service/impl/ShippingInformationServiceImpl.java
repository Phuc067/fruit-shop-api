package com.fruitshop.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fruitshop.dto.request.ShippingInformationRequest;
import com.fruitshop.entity.ShippingInformation;
import com.fruitshop.entity.User;
import com.fruitshop.mapper.ShippingInformationMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.ShippingInformationRepository;
import com.fruitshop.repository.UserRepository;
import com.fruitshop.service.ShippingInformationService;
import com.fruitshop.utils.AuthenticationUtils;

@Service
public class ShippingInformationServiceImpl implements ShippingInformationService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShippingInformationRepository shippingInformationRepository;

	@Override
	public ResponseObject getShippingInformation(Integer userId, Boolean isPrimary) {
		
		Optional<User> userDB = userRepository.findById(userId);
		if(userDB.isEmpty()) return new ResponseObject(HttpStatus.NOT_FOUND, "Không tồn tại người dùng có id là " + userId, null);
		String username = userDB.get().getLogin().getUsername();
		
		if(!AuthenticationUtils.isAuthenticate(username))return new ResponseObject(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên", null);
		
		if(ObjectUtils.isEmpty(isPrimary) || !isPrimary) 
		{
			List<ShippingInformation> shippingInformations = shippingInformationRepository.findByUserId(userId);
			return new ResponseObject(HttpStatus.OK, "Lấy danh sách địa chỉ nhận hàng thành công", shippingInformations);
		}

		ShippingInformation shippingInformation = shippingInformationRepository.findByUserIdAndIsPrimary(userId, isPrimary);
		if(ObjectUtils.isNotEmpty(shippingInformation)) {
			return new ResponseObject(HttpStatus.OK, "Lấy địa chỉ nhận hàng thành công", shippingInformation);
		}
		List<ShippingInformation> shippingInformations = shippingInformationRepository.findByUserId(userId);
		if(shippingInformations.size() > 0) 
			return new ResponseObject(HttpStatus.OK, "Lấy địa chỉ nhận hàng thành công", shippingInformations.get(0));
		else 
			return new ResponseObject(HttpStatus.OK, "Người dùng này chưa có địa chỉ nhận hàng", null);
	}

	@Override
	public ResponseObject createShippingInformation(ShippingInformationRequest request) {
		Optional<User> userDB = userRepository.findById(request.getUserId());
		if(userDB.isEmpty()) return new ResponseObject(HttpStatus.NOT_FOUND, "Không tồn tại người dùng có id là " + request.getUserId(), null);
		String username = userDB.get().getLogin().getUsername();
		if(!AuthenticationUtils.isAuthenticate(username))return new ResponseObject(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên", null);
		ShippingInformation shippingInformation = ShippingInformationMapper.INSTANT.toShippingInformation(request);
		shippingInformationRepository.save(shippingInformation);
		return new ResponseObject(HttpStatus.CREATED, "Thêm địa chỉ nhận hàng thành công", null);
	}

	
}
