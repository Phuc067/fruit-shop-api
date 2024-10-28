package com.fruitshop.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fruitshop.dto.request.LoginRequest;
import com.fruitshop.dto.response.AuthenticationResponse;
import com.fruitshop.entity.Login;
import com.fruitshop.entity.RefreshToken;
import com.fruitshop.entity.User;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.CartDetailRepository;
import com.fruitshop.repository.UserRepository;
import com.fruitshop.service.AuthenticationService;
import com.fruitshop.service.JwtService;
import com.fruitshop.service.RefreshTokenService;


@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	
	@Autowired
	private AuthenticationManager authenticationManager;

	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private CartDetailRepository cartDetailRepository;
	@Override
	public ResponseObject login(LoginRequest loginRequest) {
		
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		Login login = (Login) authentication.getPrincipal();
		if (ObjectUtils.isNotEmpty(login) && !login.getState()) {
			return new ResponseObject(HttpStatus.UNAUTHORIZED,"Tài khoản chưa được xác thực, xin vui lòng xác thực tài khoản.", null);
		}
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(login);
		String jwtToken = "Bearer " + jwtService.generateToken(login);
		
		User user = userRepository.findByLogin(login);
		int cartItem = 0;
		if(ObjectUtils.isNotEmpty(user)) cartItem =  cartDetailRepository.getCountProductByUserId(user.getId());
		
		return new ResponseObject(HttpStatus.OK, "Đăng nhập thành công.", new AuthenticationResponse(jwtToken, refreshToken.getToken(), user, cartItem));
	}


	
	@Override
	public ResponseObject logOut(String authHeader) {
		String token = authHeader.replace("Bearer ", "");
        jwtService.addTokenToBlacklist(token);
        return new ResponseObject(HttpStatus.OK, "Đã vô hiệu hóa token", null);
	}
}
