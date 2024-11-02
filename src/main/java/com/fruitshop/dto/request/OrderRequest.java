package com.fruitshop.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
	private Integer userId;
	private Integer shippingInformationId;
	private String paymentMethod;
	private List<Integer> cartList;
}
