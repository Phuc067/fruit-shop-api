package com.fruitshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.ShippingInformation;

@Repository
public interface ShippingInformationRepository extends JpaRepository<ShippingInformation, Integer> {

	List<ShippingInformation> findByUserId(Integer userId);

	ShippingInformation findByUserIdAndIsPrimary(Integer userId, Boolean isPrimary);

}
