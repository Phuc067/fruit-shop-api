package com.fruitshop.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fruitshop.dto.request.UserRequest;
import com.fruitshop.dto.response.UserResponse;
import com.fruitshop.entity.User;
import com.fruitshop.exception.CustomException;
import com.fruitshop.mapper.UserMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.UserRepository;
import com.fruitshop.service.UserService;
import com.fruitshop.utils.AuthenticationUtils;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public ResponseObject updateUser(Integer userId, UserRequest request) {
		
		Optional<User> userDB = userRepository.findById(userId);
		if(userDB.isEmpty())  throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng");
		User user = userDB.get();
		
		if(!AuthenticationUtils.isAuthenticate(user.getLogin().getUsername()))
			throw new CustomException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này");
		
		User userUpdate = UserMapper.INSTANT.requestToEntity(request);
		userUpdate.setId(userId);
		userUpdate.setLogin(user.getLogin());
		userRepository.save(userUpdate);
		UserResponse  response = UserMapper.INSTANT.entityToResponse(userUpdate);
		return new ResponseObject(HttpStatus.ACCEPTED, "Cập nhật thông tin thành công",response);
	}

}
