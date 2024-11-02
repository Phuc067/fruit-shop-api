package com.fruitshop.constant;

public enum PaymentMethod {
	COD("cod"),
	VNPAY("vnpay");
	
	private final String displayName;
	
	PaymentMethod(String diplayName){
		this.displayName = diplayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}

