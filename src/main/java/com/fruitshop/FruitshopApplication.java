package com.fruitshop;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import com.fruitshop.dto.request.ProductRequest;
import com.fruitshop.entity.Product;
import com.fruitshop.repository.ProductRepository;
import com.fruitshop.repository.SpRepository;
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
import com.fruitshop.utils.TimeUtils;

import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@DependsOn("applicationContextProvider")
public class FruitshopApplication {

  @Autowired
  private SpRepository spRepository;

	public static void main(String[] args) {
		SpringApplication.run(FruitshopApplication.class, args);
	}

	@PostConstruct
	public void run() {
//    List<ProductRequest> products = spRepository.getPageProduct(0,10,"",1);
//    for (ProductRequest product : products) {
//    System.out.println("ID: " + product.getId());
//    System.out.println("Title: " + product.getTitle());
//    System.out.println("Description: " + product.getDescription());
//    System.out.println("Origin: " + product.getOrigin());
//    System.out.println("Price: " + product.getPrice());
//    System.out.println("Quantity: " + product.getQuantity());
//    System.out.println("Image: " + product.getImage());
//    System.out.println("Category ID: " + (product.getCategoryId() != null ? product.getCategoryId(): "No category"));
//    System.out.println("-------------------------------");
//}
	}
}
