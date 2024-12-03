package com.fruitshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;
	
	private final AuthenticationProvider authenticationProvider;

    @SuppressWarnings("removal")
    @Bean
    SecurityFilterChain sFilterChain(HttpSecurity http) throws Exception
	{
		http
			.cors()
			.and()
			.csrf()
			.disable()
			.authorizeHttpRequests()
			.requestMatchers(ApiPath.LOGIN,
							ApiPath.REFRESH_TOKEN,
							ApiPath.PUBLIC + "**").permitAll()
			.requestMatchers(HttpMethod.POST, ApiPath.PRODUCT).hasRole("ADMIN")
			.anyRequest().authenticated()
				.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}

