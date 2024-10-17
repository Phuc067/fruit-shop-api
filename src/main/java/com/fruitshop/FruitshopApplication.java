package com.fruitshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;


import jakarta.annotation.PostConstruct;

@SpringBootApplication
@DependsOn("applicationContextProvider")
public class FruitshopApplication {

//	@Autowired
//	private CategoryRepository categoryRepository;
	public static void main(String[] args) {
		SpringApplication.run(FruitshopApplication.class, args);
	}
	
	@PostConstruct
	public void run() {

	}
}
