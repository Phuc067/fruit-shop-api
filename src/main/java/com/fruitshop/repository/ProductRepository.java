package com.fruitshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fruitshop.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query(value = "CALL sp_get_discount_of_product(:id)", nativeQuery = true)
	int getProductDiscount(@Param("id") Integer id);

	Boolean existsByTitle(String title);

//	@Query("SELECT p FROM Product p " + "LEFT JOIN DiscountDetail dd ON p = dd.product "
//			+ "LEFT JOIN Discount d ON dd.discount = d "
//			+ "AND (d.effectiveDate IS NULL OR d.effectiveDate <= CURRENT_TIMESTAMP) "
//			+ "AND (d.expiryDate IS NULL OR d.expiryDate >= CURRENT_TIMESTAMP) "
//			+ "WHERE (:keyword IS NULL OR p.title LIKE CONCAT('%', :keyword, '%')) " + "ORDER BY CASE "
//			+ "WHEN :sortType IS NULL THEN p.id "
//			+ "WHEN :sortType = 'price_asc' THEN (p.price * (1 - COALESCE(d.value, 0) / 100)) "
//			+ "WHEN :sortType = 'price_desc' THEN -(p.price * (1 - COALESCE(d.value, 0) / 100)) "
//			+ "WHEN :sortType = 'discount_asc' THEN COALESCE(d.value, 0) "
//			+ "WHEN :sortType = 'discount_desc' THEN -COALESCE(d.value, 0) " + "ELSE p.id END ASC, p.id ASC")
//	Page<Product> findProductsWithPaginationAndSorting(@Param("keyword") String keyword,
//			@Param("sortType") String sortType, Pageable pageable);
	
	@Query(value = "SELECT p " + 
            "FROM Product p " + 
            "LEFT JOIN DiscountDetail dd ON p = dd.product " +
            "LEFT JOIN Discount d ON dd.discount = d " +
            "AND (d.effectiveDate IS NULL OR d.effectiveDate <= CURRENT_TIMESTAMP) " +
            "AND (d.expiryDate IS NULL OR d.expiryDate >= CURRENT_TIMESTAMP) " +
            "WHERE (:keyword IS NULL OR p.title LIKE :keyword) " + 
            "ORDER BY CASE " +
            "WHEN :sortType = 0 THEN p.id " +
            "WHEN :sortType = 1 THEN p.price * (1 - COALESCE(d.value, 0) / 100) " +  // price_asc
            "WHEN :sortType = 2 THEN -(p.price * (1 - COALESCE(d.value, 0) / 100)) " + // price_desc
            "WHEN :sortType = 3 THEN COALESCE(d.value, 0) " +  // discount_asc
            "WHEN :sortType = 4 THEN -COALESCE(d.value, 0) " + // discount_desc
            "ELSE p.id END ASC, p.id ASC")
Page<Product> findProductsWithPaginationAndSorting(
     @Param("keyword") String keyword, 
     @Param("sortType") Integer sortType, 
     Pageable pageable);



}
