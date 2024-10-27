package com.Batman.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


import lombok.RequiredArgsConstructor;

/**
 * 
 * A aspect to maintain the synchronization between all instances on the cache load by a custom algorithm
 * Simply the sync attribute for all instances of service using the cache
 * 
 * 
 */
@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class DistributedCacheAspect {
	
	@SuppressWarnings("unused")
	private final CacheManager cacheManager;
	
	
	@Around(value = "@annotation(CacheDistribute)")
	public Object distributeCaching(ProceedingJoinPoint joinPoint) {
		
		@SuppressWarnings("unused")
		String accessKey = joinPoint.getSignature().toLongString() + Arrays.toString(joinPoint.getArgs());
		
		
		return joinPoint;
		
	}

}
