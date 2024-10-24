package com.fruitshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.fruitshop.dto.request.CartRequest;
import com.fruitshop.dto.response.CartResponse;
import com.fruitshop.entity.CartDetail;
import com.fruitshop.entity.Product;
import com.fruitshop.entity.User;

@Mapper(componentModel = "spring")
public interface CartMapper {

	CartMapper INSTANT = Mappers.getMapper(CartMapper.class);

	@Mapping(target = "user", source = "userId", qualifiedByName = "mapUserIdToUser")
	@Mapping(target = "product", source = "productId", qualifiedByName = "mapProductIdToProduct")
	CartDetail toCartDetail(CartRequest cartRequest);

	@Named("mapUserIdToUser")
	default User mapUserIdToUser(Integer userId) {
		if (userId == null) {
			return null;
		}
		return new User(userId);
	}

	@Named("mapProductIdToProduct")
	default Product mapProductIdToProduct(Integer productId) {
		if (productId == null) {
			return null;
		}
		return new Product(productId);
	}
	
	
	 @Mapping(source = "product", target = "product")
	    CartResponse toCartResponse(CartDetail cartDetail);
}
