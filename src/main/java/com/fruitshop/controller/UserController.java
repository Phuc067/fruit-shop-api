package com.fruitshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.dto.request.UserRequest;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.UserService;

@RestController
@RequestMapping(ApiPath.USER)
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseObject> updateUserProfile(@PathVariable("id") Integer id, @RequestBody UserRequest request){
		ResponseObject responseObject = userService.updateUser(id, request);
		return ResponseEntity.ok(responseObject);
	}
}
