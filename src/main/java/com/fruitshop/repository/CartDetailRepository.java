package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.CartDetail;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {

	@Query(value = "Select count(*) from cart_details where user_id = ?1", nativeQuery = true)
	int getCountProductByUserId(int id);

	CartDetail findByUserIdAndProductId(Integer productId, Integer userId);

}
