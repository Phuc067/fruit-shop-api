package com.fruitshop.constant;

import org.springframework.http.HttpStatus;

import com.fruitshop.exception.CustomException;

public enum OrderStatus {
	PENDING("Pending"), 
	AWAITING_PAYMENT("AwaitingPayment"),
	SHIPPING("Shipping"), 
	DELIVERED("Delivered"),
	CANCELED("Cancelled");
//	RETURNED("Returned"), 
//	REFUNDED("Refunded");

	private final String displayName;

	OrderStatus(String displayName) {
		this.displayName = displayName;
	}
	
	public static OrderStatus fromDisplayName(String displayName) {
	    for (OrderStatus status : OrderStatus.values()) {
	        if (status.getDisplayName().equalsIgnoreCase(displayName)) {
	            return status;
	        }
	    }
	    throw new CustomException( HttpStatus.NOT_FOUND,"No enum constant with: " + displayName);
	}


	public String getDisplayName() {
		return displayName;
	}
	
}