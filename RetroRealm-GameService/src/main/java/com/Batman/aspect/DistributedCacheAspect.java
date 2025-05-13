package com.Batman.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.Ordered;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.core.annotation.Order;


import lombok.RequiredArgsConstructor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * An aspect to maintain the synchronization between all instances on the cache load by a custom algorithm
 * Simply the sync attribute for all instances of service using the cache
 */

@Slf4j
@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class DistributedCacheAspect {

    @SuppressWarnings("unused")
    private final CacheManager cacheManager;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final StandardReflectionParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();


    @Around(value = "@annotation(CacheDistribute)")
    public Object distributeCaching(ProceedingJoinPoint joinPoint) {
        log.info("Entering Distributed Cache Aspect distribute Caching .....");
        try {
            Object[] args = joinPoint.getArgs();
            String accessKey = joinPoint.getSignature().toLongString() + Arrays.toString(args);
            log.debug("Access Key for the Cache :: {}", accessKey);

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Cacheable cacheable = method.getDeclaredAnnotation(Cacheable.class);
            String spel = cacheable.key();
            log.debug("Cacheable Key SPEL :: {}", spel);

            String key = parseSpel(method, args, spel);
            String cacheValue = cacheable.value()[0];
            Cache.ValueWrapper valueWrapper = Objects.requireNonNull(cacheManager.getCache(cacheValue)).get(key);
            if (Objects.nonNull(valueWrapper))
                return joinPoint.proceed();
            if (cacheManager.getCache("ACCESS_CACHE").get(accessKey) != null) {
//                waitForSynchronization("ACCESS_CACHE", accessKey);
                return joinPoint.proceed();
            } else {
                cacheManager.getCache("ACCESS_CACHE").put(accessKey, true);
                Object proceed = joinPoint.proceed();
//                cacheManager.getCache(DISTRIBUTED_CACHE_NAME).evict(accessKey);
                return proceed;
            }
        } catch (Throwable e) {
            log.error("Error in Cache Distribute Aspect {}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        } finally {
            log.info("Leaving Distributed Cache Aspect distribute Caching .....");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T parseSpel(Method method, Object[] arguments, String spel) {
        String[] params = discoverer.getParameterNames(method);
        if (Objects.isNull(params)) {
            log.error("Method Params are null");
            throw new IllegalArgumentException("Method Params are null");
        }
        EvaluationContext context = new StandardEvaluationContext();

        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], arguments[len]);
        }

        Expression expression = parser.parseExpression(spel);
        return expression.getValue(context, (Class<T>) String.class);
    }

}
