package com.fruitshop.service;

import com.fruitshop.model.ResponseObject;

public interface ReportService {
	ResponseObject reportOrderByMonth(Integer month, Integer year);
}
