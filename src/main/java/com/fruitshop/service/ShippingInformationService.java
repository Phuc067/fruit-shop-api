package com.fruitshop.service;

import com.fruitshop.dto.request.ShippingInformationRequest;
import com.fruitshop.model.ResponseObject;

public interface ShippingInformationService {
	ResponseObject getShippingInformation(Integer userId, Boolean isPrimary);

	ResponseObject createShippingInformation(ShippingInformationRequest request);
}
