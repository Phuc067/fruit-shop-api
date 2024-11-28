package com.fruitshop.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
	private String title;
	private String description;
	private String origin;
	private Double price;
	private Integer quantity;
	private String image;
	private Integer category;
}
