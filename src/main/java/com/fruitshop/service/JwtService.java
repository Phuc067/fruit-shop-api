package com.fruitshop.service;

import java.util.Map;
import java.util.function.Function;


import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JwtService {
	String extractUsername(String token);
	String extractRole(String token);
	<T> T extractClaims(String token, Function<Claims,T > claimsResolver);
	String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
	String generateToken(UserDetails userDetails);
	Boolean isTokenValid(String token, UserDetails userDetails);
	Boolean isTokenExpired(String token);
	
	public void addTokenToBlacklist(String token);
	public boolean isTokenBlacklisted(String token);
}
