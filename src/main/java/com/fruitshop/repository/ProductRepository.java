package com.fruitshop.repository;

import java.util.List;
import java.util.Optional;

import com.fruitshop.model.ProductIdNameProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.fruitshop.entity.Product;

import jakarta.persistence.LockModeType;
import org.springframework.transaction.annotation.Transactional;

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

  @Query("SELECT COUNT(p) FROM Product p WHERE p.title LIKE :keyword")
  long countByKeyword(@Param("keyword") String keyword);

  Product findByTitle(String title);

  List<ProductIdNameProjection> findAllBy();
}
