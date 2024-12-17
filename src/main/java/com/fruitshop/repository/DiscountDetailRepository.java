package com.fruitshop.repository;

import com.fruitshop.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.DiscountDetail;

import java.util.List;

@Repository
public interface DiscountDetailRepository extends JpaRepository<DiscountDetail, Integer>{

  List<DiscountDetail> findByDiscount(Discount discount);
}
