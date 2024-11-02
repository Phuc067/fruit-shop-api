package com.fruitshop.mapper;

import java.time.Instant;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.fruitshop.dto.request.OrderRequest;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.User;
import com.fruitshop.utils.TimeUtils;

public interface OrderMapper {
	OrderMapper INSTANT = Mappers.getMapper(OrderMapper.class);
	
	
	@Mapping(target = "user", source = "userId", qualifiedByName = "mapUserIdToUser")
	@Mapping(target = "orderDate", qualifiedByName = "getOrderDate" )
	@Mapping(target = "state" ,defaultExpression = "java(OrderStatus.PENDING)")
	Order requestToEntity(OrderRequest request);
	
	@Named("mapUserIdToUser")
	default User mapUserIdToUser(Integer userId) {
		if (userId == null) {
			return null;
		}
		return new User(userId);
	}
	
	@Named("getOrderDate")
	default Instant getNow() {
		return TimeUtils.getInstantNow();
	}

}
