package com.fruitshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.dto.request.ShippingInformationRequest;
import com.fruitshop.dto.request.ShippingInformationUpdate;
import com.fruitshop.entity.ShippingInformation;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.ShippingInformationService;

@RestController
@RequestMapping(ApiPath.SHIPPING_INFORMATION)
public class ShippingInformationController {
	
	@Autowired
	private ShippingInformationService shippingInformationService;

	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getShippingInformationOfUser(@PathVariable("id") Integer userId, @RequestParam(value = "isPrimary", required=false) Boolean isPrimary )
	{
		ResponseObject responseObject = shippingInformationService.getShippingInformation(userId, isPrimary);
		return ResponseEntity.ok(responseObject);
	}
	
	@PostMapping("")
	public ResponseEntity<ResponseObject> createShippingInformation(@RequestBody ShippingInformationRequest request)
	{
		ResponseObject responseObject = shippingInformationService.createShippingInformation(request);
		return ResponseEntity.ok(responseObject);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseObject> changeShippingInformation(@PathVariable("id") Integer id, @RequestBody ShippingInformationUpdate request)
	{
		ResponseObject responseObject = shippingInformationService.updateShippingInformation(id, request);
		return ResponseEntity.ok(responseObject);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseObject> deleteShippingInformation(@PathVariable("id") Integer id)
	{
		ResponseObject responseObject = shippingInformationService.deleteShippingInformation(id);
		return ResponseEntity.ok(responseObject);
	}
}
