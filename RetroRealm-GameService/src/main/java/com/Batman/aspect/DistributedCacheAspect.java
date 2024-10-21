package com.Batman.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import lombok.RequiredArgsConstructor;

@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class DistributedCacheAspect {
	
	@SuppressWarnings("unused")
	private final CacheManager cacheManager;

}
