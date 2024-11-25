package com.fruitshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.fruitshop.dto.request.UserRequest;
import com.fruitshop.dto.response.UserResponse;
import com.fruitshop.entity.User;

@Mapper
public interface UserMapper {
	UserMapper INSTANT = Mappers.getMapper(UserMapper.class);
	
	UserResponse entityToResponse(User user);
	
	User requestToEntity(UserRequest userRequest);
}
