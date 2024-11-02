package com.fruitshop.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		if (requestURI.startsWith(ApiPath.PUBLIC) || requestURI.equals(ApiPath.LOGIN) || requestURI.equals(ApiPath.REFRESH_TOKEN)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		jwt = authHeader.substring(7);
		
	
		if (jwt != null && jwtService.isTokenBlacklisted(jwt)) {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
	        return;
	    }

	    if (requestURI.equals(ApiPath.LOGOUT)) {
	        jwtService.addTokenToBlacklist(jwt);
	        response.setStatus(HttpServletResponse.SC_OK);
	        return;
	    }

	    try {
	        if (jwtService.isTokenExpired(jwt)) {
	            writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "jwt expired");
	            return;
	        }
	        username = jwtService.extractUsername(jwt);

	    } catch (ExpiredJwtException e) {
	        writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "jwt expired");
	        return;
	    }

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}
	
	private void writeJsonResponse(HttpServletResponse response, int status, String message) throws IOException {
	    response.setContentType("application/json");
	    response.setStatus(status);
	    ObjectMapper mapper = new ObjectMapper();
	    String json = mapper.writeValueAsString(Map.of("message", message));
	    response.getWriter().write(json);
	}

}
