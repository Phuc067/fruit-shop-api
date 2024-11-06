package com.fruitshop.service;

import java.io.UnsupportedEncodingException;

import com.fruitshop.model.ResponseObject;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
	ResponseObject getPaymentURL(HttpServletRequest request,Integer orderId) throws UnsupportedEncodingException;
}
