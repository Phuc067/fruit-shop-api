package com.fruitshop.service;

import com.fruitshop.entity.Login;
import com.fruitshop.entity.RefreshToken;
import com.fruitshop.model.ResponseObject;

public interface RefreshTokenService {
	RefreshToken createRefreshToken(Login login);
	ResponseObject genarateAccessToken(String request);
}
