package com.fruitshop.service;

import com.fruitshop.model.ResponseObject;

public interface ShippingInformationService {
	ResponseObject getShippingInformation(Integer userId, Boolean isPrimary);
}
