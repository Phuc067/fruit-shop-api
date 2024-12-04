package com.fruitshop.mapper;

import java.time.Instant;

import com.fruitshop.dto.response.ProductDiscount;

public class ProductDiscountMapper {
	public static ProductDiscount toProductDiscount(Object[] row) {
		if (row == null || row.length == 0) {
            return new ProductDiscount(0, null); 
        }

		int value = Integer.parseInt(row[0].toString());
		Instant expiredDate = Instant.parse(row[1].toString());

		return new ProductDiscount(value, expiredDate);
	}
}
