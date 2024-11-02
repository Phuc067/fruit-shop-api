package com.fruitshop.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fruitshop.constant.OrderStatus;
import com.fruitshop.dto.request.OrderRequest;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.ShippingInformation;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.OrderRepository;
import com.fruitshop.repository.ShippingInformationRepository;
import com.fruitshop.service.OrderService;
import com.fruitshop.utils.TimeUtils;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private ShippingInformationRepository shippingInformationRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	@Transactional
	public ResponseObject createOrder(OrderRequest request) {
		
		 Optional<ShippingInformation> shippingInfoOpt = shippingInformationRepository.findById(request.getShippingInformationId());

	        if (shippingInfoOpt.isEmpty()) {
	            throw new IllegalArgumentException("Invalid shipping information ID");
	        }

	        ShippingInformation shippingInfo = shippingInfoOpt.get();

	       
	        Order order = new Order();
	        order.setOrderDate(TimeUtils.getInstantNow());
	        order.setState(OrderStatus.PENDING); 
	        order.setRecipientName(shippingInfo.getRecipientName());
	        order.setRecipientAddress(shippingInfo.getShippingAdress());
	        order.setPhoneNumber(shippingInfo.getPhone());
	        order.setPaymentMethod(request.getPaymentMethod());
	        order.setUser(shippingInfo.getUser()); 
	        orderRepository.save(order);
	        return new ResponseObject(HttpStatus.ACCEPTED, "Tạo đơn hàng thành công", order);
	}

}
