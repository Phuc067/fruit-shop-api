package com.fruitshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fruitshop.exception.CustomException;
import com.fruitshop.model.ResponseObject;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseObject> handleCustomException(CustomException ex) {
        ResponseObject response = new ResponseObject(ex.getStatus(), ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}