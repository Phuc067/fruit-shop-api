package com.fruitshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fruitshop.dto.response.ProductResponse;
import com.fruitshop.entity.Product;
import com.fruitshop.repository.ProductRepository;


@Mapper
public interface ProductMapper {
	
	ProductMapper INSTANT = Mappers.getMapper(ProductMapper.class);
	
	ProductResponse entityToResponse(Product product);
	
}
