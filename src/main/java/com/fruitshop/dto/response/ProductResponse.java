package com.fruitshop.dto.response;

import java.time.Instant;

import com.fruitshop.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
	private Integer id;
	private String title;
	private String description;
	private String origin;
	private Double price;
	private Float discountPercentage;
	private Instant discountExpired;
	private Integer quantity;
	private String image;
	private Category category;
}
