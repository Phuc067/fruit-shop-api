package com.fruitshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	 @Query("SELECT t FROM Transaction t WHERE t.vnp_BankTranNo = :vnp_BankTranNo")
	 Optional<Transaction> findByVnpBankTranNo(@Param("vnp_BankTranNo") String vnp_BankTranNo);

	
	
}
