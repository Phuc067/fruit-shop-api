package com.fruitshop.dto.response;

import com.fruitshop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountResponse {
  private Integer id;
	private Instant effectiveDate;
	private Instant expiryDate;
	private Double value;
  private List<Product> products;
}
