package com.fruitshop.controller;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fruitshop.constant.ApiPath;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping()
public class PaymentController {
	
	@Autowired
	private VNPayService vnPayService;

	@GetMapping(ApiPath.PAY)	
	public ResponseEntity<ResponseObject> getPay(HttpServletRequest request, @RequestParam Integer orderId) throws UnsupportedEncodingException
	{
		ResponseObject responseObject = vnPayService.getPaymentURL(request, orderId);
		return ResponseEntity.ok(responseObject);
		
	}
	
}
