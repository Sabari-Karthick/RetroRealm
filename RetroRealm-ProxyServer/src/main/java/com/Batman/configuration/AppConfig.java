package com.Batman.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig  {
   

	@Bean
	RedisRateLimiter rateLimter() {
		return new RedisRateLimiter(10, 20, 1);
	}
	
//	
//	@Bean
//	RedisRateLimiter orderServiceRateLimiter() {
//		return new RedisRateLimiter(5, 20, 1);
//	}
//	
	
	@Bean
	ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService) {
		return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
	}
	
	@Bean
	PasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper;
	}
	
	@Bean
	ObjectMapper getMapper() {
		return new ObjectMapper();
	}
	
}
