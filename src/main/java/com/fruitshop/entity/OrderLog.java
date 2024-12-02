package com.fruitshop.entity;

import java.time.Instant;

import com.fruitshop.constant.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_logs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus state;
	private Instant time;
	private String PerformedBy;
	private String log;
	
	@ManyToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Order order;
}
