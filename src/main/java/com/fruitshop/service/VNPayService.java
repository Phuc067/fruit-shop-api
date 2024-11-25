package com.fruitshop.service;

import java.io.UnsupportedEncodingException;

import com.fruitshop.entity.Transaction;
import com.fruitshop.model.ResponseObject;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
	ResponseObject createPaymentURL(HttpServletRequest request,String orderId) throws UnsupportedEncodingException;

	ResponseObject addTransaction(Transaction transaction);
}
