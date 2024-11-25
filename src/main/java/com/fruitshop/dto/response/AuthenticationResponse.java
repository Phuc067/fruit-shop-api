package com.fruitshop.dto.response;

import com.fruitshop.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
	private String accessToken;
	private String refreshToken;
	private UserResponse user;
	private int cartTotal;
}
