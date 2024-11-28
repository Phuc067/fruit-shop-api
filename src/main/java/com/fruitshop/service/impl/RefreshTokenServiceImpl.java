package com.fruitshop.service.impl;

import java.time.Instant;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fruitshop.constant.SessionConstant;
import com.fruitshop.entity.Login;
import com.fruitshop.entity.RefreshToken;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.RefreshTokenRepository;
import com.fruitshop.service.JwtService;
import com.fruitshop.service.RefreshTokenService;
import com.fruitshop.utils.TimeUtils;


@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public RefreshToken createRefreshToken(Login login)
	{
		RefreshToken refreshToken = refreshTokenRepository.findByLogin(login);
		if(ObjectUtils.isEmpty(refreshToken)) 
		{
			refreshToken = new RefreshToken();
			refreshToken.setLogin(login);
		}
			
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setExpiredDate(TimeUtils.getInstantNow().plusMillis(SessionConstant.REFRESH_TOKEN_EXPRIRY_TIME));
		return refreshTokenRepository.save(refreshToken);
			
	}

	@Override
	public ResponseObject genarateAccessToken(String request) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(request);
		if(ObjectUtils.isEmpty(refreshToken) )
		{
			return new ResponseObject(HttpStatus.NOT_FOUND,"Token không hợp lệ.", null);
		}
		if(isExpired(refreshToken))
		{
			refreshTokenRepository.delete(refreshToken);
			return new ResponseObject(HttpStatus.BAD_REQUEST,"Token đã hết hạn.", null);
		}
		String accessToken = "Bearer " +  jwtService.generateToken(refreshToken.getLogin());
		return new ResponseObject(HttpStatus.OK, "Tạo access token thành công.", accessToken);
	}
	
	public Boolean isExpired(RefreshToken refreshToken)
	{
		return refreshToken.getExpiredDate().compareTo(Instant.now()) < 0 ? true : false;
	}

}
