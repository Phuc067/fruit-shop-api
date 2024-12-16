package com.fruitshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Discount;

import java.time.Instant;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    @Query("SELECT d FROM Discount d WHERE d.expiryDate > :now")
    Page<Discount> findByExpiryDateAfter(Instant now, Pageable pageable);
}
