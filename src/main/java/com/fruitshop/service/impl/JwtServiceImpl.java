package com.fruitshop.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fruitshop.constant.SessionConstant;
import com.fruitshop.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

	private static final String SECRET_KEY= "9b0018390ea10824da5c121db8fed4fbb8ac27f9cbfef612212c76c3fa94c572";
	
	private static Set<String> blackList = new HashSet<>();
	
	@Override
	public String extractUsername(String token) {
		
		return extractClaims(token, Claims::getSubject);
	}
	
	@Override
	public String extractRole(String token) {
		String role = extractClaims(token, claims -> (String) claims.get("role"));
		System.out.println("Extracted role: " + role);
		return role;
	}
	
	private Claims extractAllClaims(String token)
	{
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
		
	}
	@Override
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
	    Map<String, Object> claims = new HashMap<>(extraClaims);
	    claims.put("role", "ROLE_" + userDetails.getAuthorities().stream()
	                .map(grantedAuthority -> grantedAuthority.getAuthority())
	                .findFirst()
	                .orElse("CUSTOMER")); 
	    
	    return Jwts
	            .builder()
	            .setClaims(claims)
	            .setSubject(userDetails.getUsername()) 
	            .setIssuedAt(new Date(System.currentTimeMillis())) 
	            .setExpiration(new Date(System.currentTimeMillis() + SessionConstant.JWT_EXPIRED_TIME)) 
	            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
	            .compact();
	}


	@Override
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	@Override
	public Boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	@Override
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token)
	{
		return extractClaims(token, Claims::getExpiration);
	}
	
	@Override
	public void addTokenToBlacklist(String token) {
		blackList.add(token);
	}

	@Override
	public boolean isTokenBlacklisted(String token) {
		return blackList.contains(token);
	}
}