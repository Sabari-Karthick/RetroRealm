package com.Batman.helper;

import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;


/**
 * Helper class for cache aspect operations.
 * This class provides utility methods for parsing SpEL expressions
 * and discovering parameter names using reflection.
 *
 * <p>
 * This class is designed to be used internally by the cache aspect
 * to handle dynamic key generation and other cache-related tasks.
 * </p>
 *
 * <p>
 * **Warning:** This class is currently under development and its functionality
 * may not be fully implemented or tested. Use with caution.
 * </p>
 *
 * @author SK
 */

@SuppressWarnings("unused")
public final class CacheAspectHelper {

	private CacheAspectHelper() {}
	
	private final ExpressionParser parser = new SpelExpressionParser();
	private final StandardReflectionParameterNameDiscoverer discoverer
	                        = new StandardReflectionParameterNameDiscoverer();
	
}
