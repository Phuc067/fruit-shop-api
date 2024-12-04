package com.fruitshop.dto.response;

import java.time.Instant;
import java.util.List;

import com.fruitshop.entity.OrderDetail;
import com.fruitshop.entity.OrderLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReponse {
	private String id;
	private Instant orderDate;
	private Instant deliveryDate;
    private String state;
	private String recipientName;
	private String recipientAddress;
	private String phoneNumber;
	private String paymentMethod;
	private List<OrderDetail> orderDetails; 
	private OrderLog orderLog;
	private Boolean isPaid ;
}
