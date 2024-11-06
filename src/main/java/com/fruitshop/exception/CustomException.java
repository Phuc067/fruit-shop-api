package com.fruitshop.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

	private static final long serialVersionUID = -103544660058121456L;

	private final Integer status;

	public CustomException( HttpStatus status, String message) {
		super(message);
		this.status = status.value();
	}

	public Integer getStatus() {
		return status;
	}
}