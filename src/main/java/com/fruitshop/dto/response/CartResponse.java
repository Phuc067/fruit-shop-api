package com.fruitshop.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartResponse {
	private Integer id;
	private ProductResponse product;
	private int quantity;
}
