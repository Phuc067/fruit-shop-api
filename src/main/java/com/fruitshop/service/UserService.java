package com.fruitshop.service;

import com.fruitshop.dto.request.UserRequest;
import com.fruitshop.model.ResponseObject;

public interface UserService {
	ResponseObject updateUser(Integer userId, UserRequest request);
}
