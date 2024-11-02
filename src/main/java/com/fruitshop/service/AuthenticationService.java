package com.fruitshop.service;

import com.fruitshop.dto.request.LoginRequest;
import com.fruitshop.model.ResponseObject;

public interface AuthenticationService {
	ResponseObject login(LoginRequest loginRequest);
}
