package com.fruitshop.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReportResponse {
	private List<OrderDateCount> orderDateCounts;
	private Integer total;
	private Integer countOfCancel;
	private Double actualRevenue;
	private Double projectedRevenue;
}
