package com.fruitshop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fruitshop.entity.Product;

import jakarta.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, Integer> {


   @Query(value = "SELECT COALESCE(dc.value, 0) AS value, dc.expiry_date " +
                   "FROM discounts dc " +
                   "JOIN discount_details dc_dt ON dc.id = dc_dt.discount_id " +
                   "WHERE dc_dt.product_id = :productId " +
                   "AND DATE_ADD(NOW(), INTERVAL 7 HOUR) BETWEEN dc.effective_date AND dc.expiry_date " +
                   "ORDER BY dc.value DESC LIMIT 1", nativeQuery = true)
    Object getProductDiscount(@Param("productId") Integer productId);

  Boolean existsByTitle(String title);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Product p WHERE p.id = :productId")
  Optional<Product> findProductForUpdate(@Param("productId") Integer productId);

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
