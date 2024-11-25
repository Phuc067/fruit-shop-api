package com.fruitshop.service.impl;

import org.springframework.stereotype.Service;

import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService{

	@Override
	public ResponseObject createInvoice(Double vnp_Amount, String vnp_BankCode, String vnp_BankTranNo,
			String vnp_CardType, String vnp_OrderInfo, String vnp_PayDate, String vnp_ResponseCode,
			String vnp_TransactionNo, String vnp_TransactionStatus, String vnp_TxnRef, String vnp_SecureHash) {
		// TODO Auto-generated method stub
		return null;
	}

}
