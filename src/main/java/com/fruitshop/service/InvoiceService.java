package com.fruitshop.service;

import org.springframework.web.bind.annotation.RequestParam;

import com.fruitshop.model.ResponseObject;

public interface InvoiceService {
	ResponseObject createInvoice(Double vnp_Amount, String vnp_BankCode, String vnp_BankTranNo, String vnp_CardType,
			String vnp_OrderInfo, String vnp_PayDate, String vnp_ResponseCode, String vnp_TransactionNo,
			String vnp_TransactionStatus, String vnp_TxnRef, String vnp_SecureHash);
}
