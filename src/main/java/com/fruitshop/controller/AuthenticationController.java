package com.fruitshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.dto.request.LoginRequest;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.AuthenticationService;

@RestController
@RequestMapping(ApiPath.API_AUTH)
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<ResponseObject> login(@RequestBody LoginRequest loginRequest) {
		ResponseObject responseObject = authenticationService.login(loginRequest);
		return ResponseEntity.ok(responseObject);
	}
}
