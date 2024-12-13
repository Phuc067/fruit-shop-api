package com.fruitshop.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.fruitshop.exception.CustomException;

public enum OrderStatus {
    PENDING("pending", "Đơn hàng đã được đặt thành công"), 
    AWAITING_PAYMENT("awaitingPayment", "Đơn hàng đang chờ được thanh toán"),
    PREPARING("preparing", "Đơn hàng đang được người bán chuẩn bị"),
    SHIPPING("shipping", "Đơn hàng đang được vận chuyển"), 
    DELIVERED("delivered", "Khách hàng đã nhận đơn hàng"),
    CANCELED("cancelled", "Đơn hàng đã được hủy"),
    RETURNED("returned", "Đơn hàng đang được trả về"), 
    REFUNDED("refunded", "Đã hoàn tiền cho đơn hàng");

    private final String displayName;
    private final String logMessage;

    OrderStatus(String displayName, String logMessage) {
        this.displayName = displayName;
        this.logMessage = logMessage;
    }

    public static OrderStatus fromDisplayName(String displayName) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getDisplayName().equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        throw new CustomException(HttpStatus.NOT_FOUND, "No enum constant with: " + displayName);
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
    
    public String getLogMessage() {
        return logMessage;
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
    
    public boolean allowCancel() {
        return this == PENDING || this == AWAITING_PAYMENT || this == PREPARING;
    }
    public boolean isCanceledOrRefunded() {
    	return this == CANCELED || this == REFUNDED;
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
