package com.fruitshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingInformationUpdate {
	private String recipientName;
	private String shippingAdress;
	private String phone;
	private Boolean isPrimary;
}
