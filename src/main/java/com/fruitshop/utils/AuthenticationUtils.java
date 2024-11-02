package com.fruitshop.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class AuthenticationUtils {
	public static final boolean isAuthenticate(String username) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();
		return username.equals(authUsername);
	}
	
}
