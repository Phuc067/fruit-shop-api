package com.fruitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruitshop.entity.Invoice;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{

}
