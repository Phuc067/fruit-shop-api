package com.fruitshop.utils;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


public class AuthenticationUtils {
	public static final boolean isAuthenticate(String username) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
            System.out.println("Authentication is null");
            return false;
        }
		
		String authUsername = authentication.getName();
		return username.equals(authUsername);
	}
	
	public static final boolean isAdminAccess() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            System.out.println("Authentication is null");
            return false;
        }

        Collection<? extends GrantedAuthority> authRole = authentication.getAuthorities();
        System.out.println("AuthenticationUtils: " + authRole);

        return authRole.stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
	}
}
