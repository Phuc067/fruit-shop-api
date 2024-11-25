package com.fruitshop.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {

	private static final long serialVersionUID = 4668646270816005944L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "vnp_Amount")
	private Double vnp_Amount;
	
	@Column(name = "vnp_BankCode")
	private String vnp_BankCode;
	
	@Column(name = "vnp_BankTranNo")
	private String vnp_BankTranNo;
	
	@Column(name = "vnp_CardType")
	private String vnp_CardType;
	
	@Column(name = "vnp_OrderInfo")
	private String vnp_OrderInfo;
	
	@Column(name = "vnp_PayDate")
	private String vnp_PayDate;
	
	@Column(name = "vnp_ResponseCode")
	private String vnp_ResponseCode;
	
	@Column(name = "vnp_TmnCode")
	private String vnp_TmnCode;
	
	@Column(name = "vnp_TransactionNo")
	private String vnp_TransactionNo;
	
	@Column(name = "vnp_TransactionStatus")
	private String vnp_TransactionStatus;
	
	@Column(name = "vnp_TxnRef")
	private String vnp_TxnRef;
	
	@Column(name = "vnp_SecureHash")
	private String vnp_SecureHash;
	
	
}
