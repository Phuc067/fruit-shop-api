package com.fruitshop.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.fruitshop.exception.CustomException;

public enum OrderStatus {
	PENDING("pending"), 
	AWAITING_PAYMENT("awaitingPayment"),
	PREPARING("preparing"),
	SHIPPING("shipping"), 
	DELIVERED("delivered"),
	CANCELED("cancelled"),
	RETURNED("returned"), 
	REFUNDED("refunded");

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

	public static List<OrderStatus> parseStates(String state) {
	    return Arrays.stream(state.split("-"))
	            .map(String::trim) 
	            .map(OrderStatus::fromDisplayName) 
	            .collect(Collectors.toList());
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public OrderStatus getNextStatus() {
        switch (this) {
            case PENDING:
                return PREPARING;
            case AWAITING_PAYMENT:
                return PREPARING;
            case PREPARING:
                return SHIPPING;
            case SHIPPING:
                return DELIVERED;
            case DELIVERED:
            	return RETURNED;
            case RETURNED:
            	return REFUNDED;
            case REFUNDED:
                throw new CustomException(HttpStatus.BAD_REQUEST, "No next state for status: " + this.displayName);
            default:
                throw new IllegalStateException("Unhandled status: " + this);
        }
    }
	 public boolean requiresAdminAccess() {
	        switch (this) {
	            case PENDING:
	            case AWAITING_PAYMENT:
	            case PREPARING:
	            case RETURNED:
	                return true; 
	            default:
	                return false;
	        }
	    }
}