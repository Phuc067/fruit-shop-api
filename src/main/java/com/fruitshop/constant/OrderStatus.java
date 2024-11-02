package com.fruitshop.constant;

public enum OrderStatus {
	PENDING("Pending"), 
	CONFIRMED("Confirmed"), 
	PACKING("Packing"), 
	SHIPPED("Shipped"), 
	DELIVERED("Delivered"),
	CANCELED("Canceled"), 
	RETURNED("Returned"), 
	REFUNDED("Refunded");

	private final String displayName;

	OrderStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}