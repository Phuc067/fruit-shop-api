package com.fruitshop.controller;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fruitshop.constant.ApiPath;
import com.fruitshop.dto.request.StringRequest;
import com.fruitshop.entity.Transaction;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.InvoiceService;
import com.fruitshop.service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(ApiPath.PAY)
public class PaymentController {
	
	@Autowired
	private VNPayService vnPayService;
	
	@PostMapping("/create-payment-url")	
	public ResponseEntity<ResponseObject> getPaymentUrl(HttpServletRequest request, @RequestBody StringRequest  orderId) throws UnsupportedEncodingException
	{
		ResponseObject responseObject = vnPayService.createPaymentURL(request, orderId.getData());
		return ResponseEntity.ok(responseObject);
		
	}
	
	/**	https://localhost:8080/api/public/products?vnp_Amount=4000000&vnp_BankCode=NCB
		&vnp_BankTranNo=VNP14658165&vnp_CardType=ATM&vnp_OrderInfo=Thanh+toan+don+hang10
		&vnp_PayDate=20241108104836&vnp_ResponseCode=00&vnp_TmnCode=KLREQLOL
		&vnp_TransactionNo=14658165&vnp_TransactionStatus=00&vnp_TxnRef=86882767
		&vnp_SecureHash=c93063f272c5445d3145c21b22b04a71793e8fcd1262ced601a80d0e3582b8e308038ad5679fb6b4f1bfbbcd3cd36692e339870274d5046391708bf87926c61b
	**/
	
	@PostMapping("info")
	public ResponseEntity<ResponseObject> createPaymentInfo(@RequestBody Transaction transaction){
		ResponseObject responseObject = vnPayService.addTransaction(transaction);
		return ResponseEntity.ok(responseObject);
	}
	
}
