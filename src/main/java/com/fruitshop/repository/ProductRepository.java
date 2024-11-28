package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.fruitshop.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	@Query(value = "CALL sp_get_discount_of_product(:id)", nativeQuery = true)
	int getProductDiscount(@Param("id") Integer id);

	Boolean existsByTitle(String title);
}
