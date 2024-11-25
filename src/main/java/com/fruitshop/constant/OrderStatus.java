package com.fruitshop.constant;

public enum OrderStatus {
	PENDING("Pending"), 
	AWAITING_PAYMENT("Awaiting Payment"),
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
	
	public static OrderStatus fromDisplayName(String displayName) {
	    for (OrderStatus status : OrderStatus.values()) {
	        if (status.getDisplayName().equalsIgnoreCase(displayName)) {
	            return status;
	        }
	    }
	    throw new IllegalArgumentException("No enum constant with: " + displayName);
	}


	public String getDisplayName() {
		return displayName;
	}
	
}