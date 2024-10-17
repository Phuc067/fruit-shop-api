package com.fruitshop.model;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject {
	private int  status;
	private String message;
	private Object data;
	public ResponseObject(HttpStatus status, String message, Object data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status.value();
    }
}
