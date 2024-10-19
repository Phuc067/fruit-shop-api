package com.fruitshop.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartRequest {
	private Integer userId;
	private Integer productId;
	private int quantity;
}
