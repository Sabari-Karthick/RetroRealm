package com.Batman.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	
//	@Bean
//	AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
//			PasswordEncoder passwordEncoder) {
//		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//		authenticationProvider.setUserDetailsService(userDetailsService);
//		authenticationProvider.setPasswordEncoder(passwordEncoder);
//
//		return new ProviderManager(authenticationProvider);
//	}
//	
//	@Bean
//	PasswordEncoder bCryptPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//	
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
