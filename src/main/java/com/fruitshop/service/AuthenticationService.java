package com.fruitshop.service;

import com.fruitshop.dto.request.LoginRequest;
import com.fruitshop.dto.request.RegisterRequest;
import com.fruitshop.dto.request.VerificationRequest;
import com.fruitshop.model.ResponseObject;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
	ResponseObject login(LoginRequest loginRequest);
  	ResponseObject register(RegisterRequest registerDto) throws MessagingException;
	ResponseObject verification(VerificationRequest verificationDto) ;
	ResponseObject getNewVerification(String username) throws MessagingException;
}
