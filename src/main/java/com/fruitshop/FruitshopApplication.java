package com.fruitshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;

import com.fruitshop.repository.CartDetailRepository;
import com.fruitshop.utils.TimeUtils;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@DependsOn("applicationContextProvider")
public class FruitshopApplication {

	@Autowired
	private CartDetailRepository cartDetailRepository;
	public static void main(String[] args) {
		SpringApplication.run(FruitshopApplication.class, args);
	}
	
	@PostConstruct
	public void run() {
//		System.out.println(TimeUtils.getInstantNow());
	}
}
