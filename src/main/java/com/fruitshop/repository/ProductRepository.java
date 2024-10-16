package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fruitshop.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}
