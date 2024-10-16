package com.fruitshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fruitshop.entity.Category;
import com.fruitshop.repository.CategoryRepository;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class FruitshopApplication {

	@Autowired
	private CategoryRepository categoryRepository;
	public static void main(String[] args) {
		SpringApplication.run(FruitshopApplication.class, args);
	}
	
	@PostConstruct
	public void run() {
		Category category = new Category();
		category.setId(1);
		category.setName("Trái cây Việt Nam");
		categoryRepository.save(category);
	}
}
