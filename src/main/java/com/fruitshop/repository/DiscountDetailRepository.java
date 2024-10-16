package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.DiscountDetail;

@Repository
public interface DiscountDetailRepository extends JpaRepository<DiscountDetail, Integer>{

}
