package com.fruitshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fruitshop.entity.CartDetail;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {

	@Query(value = "Select count(*) from cart_details where user_id = ?1", nativeQuery = true)
	int getCountProductByUserId(int id);

	CartDetail findByUserIdAndProductId(Integer userId, Integer productId);

	List<CartDetail> findByUserId(int userId);
	
	@Transactional
    @Modifying
    @Query("DELETE FROM CartDetail c WHERE c.id IN :ids")
    void deleteCartItemsByIds(List<Integer> ids);

}
