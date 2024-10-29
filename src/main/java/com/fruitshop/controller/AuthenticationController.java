package com.fruitshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.dto.request.LoginRequest;
import com.fruitshop.dto.request.RefreshTokenRequest;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.AuthenticationService;
import com.fruitshop.service.RefreshTokenService;


@RestController
@RequestMapping(ApiPath.AUTH)
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("login")
	public ResponseEntity<ResponseObject> login(@RequestBody LoginRequest loginRequest) {
		ResponseObject responseObject = authenticationService.login(loginRequest);
		return ResponseEntity.ok(responseObject);
	}
	@PostMapping("refresh-token")
	public ResponseEntity<ResponseObject> getRefreshToken(@RequestBody RefreshTokenRequest request)
	{
		ResponseObject responseObject =  refreshTokenService.genarateAccessToken(request.getRefreshToken());
		return ResponseEntity.ok(responseObject);
	}
	
	@GetMapping("")
	public ResponseEntity<ResponseObject> logout(@RequestHeader("Authorization") String authHeader){
		ResponseObject responseObject = authenticationService.logOut(authHeader);
		return ResponseEntity.ok(responseObject);
	}
}
