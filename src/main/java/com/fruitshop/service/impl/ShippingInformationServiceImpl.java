package com.fruitshop.service.impl;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fruitshop.entity.ShippingInformation;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.ShippingInformationRepository;
import com.fruitshop.service.ShippingInformationService;

@Service
public class ShippingInformationServiceImpl implements ShippingInformationService{
	
	@Autowired
	private ShippingInformationRepository shippingInformationRepository;

	@Override
	public ResponseObject getShippingInformation(Integer userId, Boolean isPrimary) {
		if(!isPrimary) 
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

	
}
