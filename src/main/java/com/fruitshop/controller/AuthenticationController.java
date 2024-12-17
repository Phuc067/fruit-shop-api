package com.fruitshop.controller;

import com.fruitshop.dto.request.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.AuthenticationService;
import com.fruitshop.service.RefreshTokenService;


@RestController
@RequestMapping("")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping(ApiPath.LOGIN)
	public ResponseEntity<ResponseObject> login(@RequestBody LoginRequest loginRequest) {
		ResponseObject responseObject = authenticationService.login(loginRequest);
		return ResponseEntity.ok(responseObject);
	}
	@PostMapping(ApiPath.REFRESH_TOKEN)
	public ResponseEntity<ResponseObject> getRefreshToken(@RequestBody RefreshTokenRequest request)
	{
		ResponseObject responseObject =  refreshTokenService.genarateAccessToken(request.getRefreshToken());
		return ResponseEntity.ok(responseObject);
	}

  @PostMapping(ApiPath.REGISTER)
  public  ResponseEntity<ResponseObject> register(@RequestBody RegisterRequest request) throws MessagingException {
    ResponseObject responseObject = authenticationService.register(request);
    return ResponseEntity.ok(responseObject);
  }

  @PostMapping(ApiPath.VERIFICATION)
  public ResponseEntity<ResponseObject> verification(@RequestBody VerificationRequest request)
  {
    ResponseObject responseObject = authenticationService.verification(request);
    return ResponseEntity.ok(responseObject);
  }

  @PutMapping(ApiPath.VERIFICATION)
  public ResponseEntity<ResponseObject> getNewVerification(@RequestBody String username) throws MessagingException
  {
    ResponseObject responseObject = authenticationService.getNewVerification(username);
    return ResponseEntity.ok(responseObject);
  }

	@PostMapping(ApiPath.LOGOUT)
	public ResponseEntity<ResponseObject> logout(@RequestBody LogoutRequest request){
		ResponseObject responseObject = authenticationService.logOut(request);
		return ResponseEntity.ok(responseObject);
	}
}
