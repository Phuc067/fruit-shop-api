package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Invoice;
import com.fruitshop.entity.Order;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{

	Invoice findByOrder(Order order);

	Boolean existsByOrderId(String id);

}
