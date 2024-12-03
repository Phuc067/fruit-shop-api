package com.fruitshop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fruitshop.dto.request.ProductRequest;
import com.fruitshop.dto.response.ProductResponse;
import com.fruitshop.entity.Category;
import com.fruitshop.entity.Product;
import com.fruitshop.repository.ProductRepository;


@Mapper
public interface ProductMapper {
	
	ProductMapper INSTANT = Mappers.getMapper(ProductMapper.class);
	
	ProductResponse entityToResponse(Product product);
	
	@Mapping(source = "category", target = "category", qualifiedByName = "mapCategory")
	Product requestToEnity(ProductRequest request);
	
	
	@Named("mapCategory")
	default Category mapCategory(Integer categoryId) {
		return new Category(categoryId);
	}
}
