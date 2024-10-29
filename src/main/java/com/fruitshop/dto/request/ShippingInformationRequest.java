package com.fruitshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingInformationRequest {
	private String recipientName;
	private String shippingAdress;
	private String phone;
	private Integer userId;
}
