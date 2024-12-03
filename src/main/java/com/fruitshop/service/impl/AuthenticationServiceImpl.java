package com.fruitshop.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fruitshop.dto.request.LoginRequest;
import com.fruitshop.dto.response.AuthenticationResponse;
import com.fruitshop.dto.response.UserResponse;
import com.fruitshop.entity.Login;
import com.fruitshop.entity.RefreshToken;
import com.fruitshop.entity.User;
import com.fruitshop.mapper.UserMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.CartDetailRepository;
import com.fruitshop.repository.RefreshTokenRepository;
import com.fruitshop.repository.UserRepository;
import com.fruitshop.service.AuthenticationService;
import com.fruitshop.service.JwtService;
import com.fruitshop.service.RefreshTokenService;
import com.fruitshop.utils.AuthenticationUtils;


@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	
	@Autowired
	private AuthenticationManager authenticationManager;

	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

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
		
		refreshTokenRepository.save(refreshToken);
		String jwtToken = "Bearer " + jwtService.generateToken(login);
		
		User user = userRepository.findByLogin(login);
		UserResponse userResponse = UserMapper.INSTANT.entityToResponse(user);
		userResponse.setUsername(loginRequest.getUsername());
		int cartItem = 0;
		if(ObjectUtils.isNotEmpty(user)) cartItem =  cartDetailRepository.getCountProductByUserId(user.getId());
		
		return new ResponseObject(HttpStatus.OK, "Đăng nhập thành công.", new AuthenticationResponse(jwtToken, refreshToken.getToken(), userResponse, cartItem));
	}
}
