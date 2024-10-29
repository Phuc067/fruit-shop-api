package com.fruitshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.fruitshop.dto.request.ShippingInformationRequest;
import com.fruitshop.entity.ShippingInformation;
import com.fruitshop.entity.User;

@Mapper
public interface ShippingInformationMapper {
	ShippingInformationMapper INSTANT = Mappers.getMapper(ShippingInformationMapper.class);
	
	
	@Mapping(target = "user", source = "userId", qualifiedByName = "mapUserIdToUser")
	  @Mapping(target = "isPrimary", constant = "false")
	ShippingInformation toShippingInformation(ShippingInformationRequest request);
	
	@Named("mapUserIdToUser")
	default User mapUserIdToUser(Integer userId) {
		if (userId == null) {
			return null;
		}
		return new User(userId);
	}
}
