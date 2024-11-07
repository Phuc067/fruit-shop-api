package com.fruitshop.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fruitshop.config.VNPAYConfig;
import com.fruitshop.constant.PaymentMethod;
import com.fruitshop.entity.Invoice;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderDetail;
import com.fruitshop.exception.CustomException;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.InvoiceRepository;
import com.fruitshop.repository.OrderDetailRepository;
import com.fruitshop.repository.OrderRepository;
import com.fruitshop.service.OrderService;
import com.fruitshop.service.VNPayService;
import com.fruitshop.utils.AuthenticationUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class VNPayServiceImpl implements VNPayService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Override
	public ResponseObject createPaymentURL(HttpServletRequest request, Integer orderId) throws UnsupportedEncodingException {
		String orderType = "other";
		
		Optional<Order> orderDB = orderRepository.findById(orderId);
		if(orderDB.isEmpty()) throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng");
		
		Order order = orderDB.get();
		String username =  order.getUser().getLogin().getUsername();
		if(!AuthenticationUtils.isAuthenticate(username)) 
			 throw new CustomException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này");
		
		if(!order.getPaymentMethod().equals(PaymentMethod.VNPAY.getDisplayName()))
			 return new ResponseObject(HttpStatus.BAD_REQUEST, "Đơn hàng này không được thanh toán bằng VNpay", null);
		
		Invoice invoice = invoiceRepository.findByOrder(order);
		if(ObjectUtils.isNotEmpty(invoice))
			return new ResponseObject(HttpStatus.ALREADY_REPORTED, "Đơn hàng này đã được thanh toán thành công", null);
		
		int amount = 0;
		List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
		
		for(OrderDetail orderDetail: orderDetails) {
			amount += orderDetail.getQuantity() * orderDetail.getPrice();
		}
		amount = amount * 100;
		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());
		
		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", VNPAYConfig.vnp_Version);
		vnp_Params.put("vnp_Command", VNPAYConfig.vnp_Command);
		vnp_Params.put("vnp_TmnCode", VNPAYConfig.vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount));
		vnp_Params.put("vnp_CurrCode", "VND");
		vnp_Params.put("vnp_BankCode", "NCB");
		vnp_Params.put("vnp_TxnRef", VNPAYConfig.getRandomNumber(8));
		vnp_Params.put("vnp_OrderInfo","Thanh toan don hang" +  orderId);
		vnp_Params.put("vnp_OrderType", orderType);
		vnp_Params.put("vnp_Locale", "vn");
		vnp_Params.put("vnp_ReturnUrl", VNPAYConfig.vnp_Returnurl);
		vnp_Params.put("vnp_IpAddr", VNPAYConfig.getIpAddress(request));
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
		
		String paymentUrl = VNPAYConfig.vnp_PayUrl + "?" + VNPAYConfig.hashAllField(vnp_Params);
		return new ResponseObject(HttpStatus.OK, "Lấy url thanh toán thành công", paymentUrl);
	}
}