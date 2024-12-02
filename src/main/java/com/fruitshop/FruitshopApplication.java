package com.fruitshop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;

import com.fruitshop.constant.OrderStatus;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderLog;
import com.fruitshop.repository.OrderLogRepository;
import com.fruitshop.repository.OrderRepository;
import com.fruitshop.utils.RandomUtils;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@DependsOn("applicationContextProvider")
public class FruitshopApplication {


	public static void main(String[] args) {
		SpringApplication.run(FruitshopApplication.class, args);
	}

	@PostConstruct
	public void run() {
//		List<Order> orders = orderRepository.findAll();
//
//		for (Order order : orders) {
//			OrderStatus state = order.getState();
//			OrderLog orderLog =  OrderLog.builder().state(state).log(state.getLogMessage())
//					.PerformedBy(order.getUser().getLogin().getUsername()).time(order.getOrderDate()).order(order).build();
//			orderLogRepository.save(orderLog);
//		}
	}
}
