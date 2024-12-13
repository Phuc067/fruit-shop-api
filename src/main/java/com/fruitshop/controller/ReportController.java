package com.fruitshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.ReportService;

@RestController
@RequestMapping(ApiPath.REPORT)
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	
	
	
	@GetMapping("/order-by-month-and-year")
	ResponseEntity<ResponseObject> getOrderReportByMonthAndYear(@RequestParam("month") Integer month, @RequestParam("year") Integer year)
	{
		ResponseObject responseObject = reportService.reportOrderByMonth(month, year);
		return ResponseEntity.ok(responseObject);
	}
}
