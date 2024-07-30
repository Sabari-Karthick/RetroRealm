package com.Batman.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	
}
