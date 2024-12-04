package com.fruitshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fruitshop.dto.response.ProductDiscount;
import com.fruitshop.entity.Product;
import com.fruitshop.mapper.ProductDiscountMapper;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query(value = "CALL sp_get_discount_of_product(:id)", nativeQuery = true)
	Object[] getProductDiscountRaw(@Param("id") Integer id);

	default ProductDiscount getProductDiscount(@Param("id") Integer id) {
		 Object[] row = getProductDiscountRaw(id);
	        return ProductDiscountMapper.toProductDiscount(row);
	}

	Boolean existsByTitle(String title);


	@Query(value = "SELECT p " + "FROM Product p " + "LEFT JOIN DiscountDetail dd ON p = dd.product "
			+ "LEFT JOIN Discount d ON dd.discount = d "
			+ "AND (d.effectiveDate IS NULL OR d.effectiveDate <= CURRENT_TIMESTAMP) "
			+ "AND (d.expiryDate IS NULL OR d.expiryDate >= CURRENT_TIMESTAMP) "
			+ "WHERE (:keyword IS NULL OR p.title LIKE :keyword) " + "ORDER BY CASE " + "WHEN :sortType = 0 THEN p.id "
			+ "WHEN :sortType = 1 THEN p.price * (1 - COALESCE(d.value, 0) / 100) " + // price_asc
			"WHEN :sortType = 2 THEN -(p.price * (1 - COALESCE(d.value, 0) / 100)) " + // price_desc
			"WHEN :sortType = 3 THEN COALESCE(d.value, 0) " + // discount_asc
			"WHEN :sortType = 4 THEN -COALESCE(d.value, 0) " + // discount_desc
			"ELSE p.id END ASC, p.id ASC")
	Page<Product> findProductsWithPaginationAndSorting(@Param("keyword") String keyword,
			@Param("sortType") Integer sortType, Pageable pageable);

}
