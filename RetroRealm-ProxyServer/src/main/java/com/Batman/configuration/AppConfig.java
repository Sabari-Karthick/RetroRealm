package com.Batman.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppConfig {
  
	@Bean(name = "gameLimiter")
	@Primary
	RedisRateLimiter gameServiceRateLimit() {
		return new RedisRateLimiter(10, 20, 1);
	}
	
	
	@Bean(name = "orderLimiter")
	RedisRateLimiter orderServiceRateLimiter() {
		return new RedisRateLimiter(5, 20, 1);
	}
	
	
}
