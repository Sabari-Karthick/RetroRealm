package com.Batman.helper;

import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@SuppressWarnings("unused")
public final class CacheAspectHelper {

	private CacheAspectHelper() {}
	
	private final ExpressionParser parser = new SpelExpressionParser();
	private final StandardReflectionParameterNameDiscoverer discoverer
	                        = new StandardReflectionParameterNameDiscoverer();
	
}
