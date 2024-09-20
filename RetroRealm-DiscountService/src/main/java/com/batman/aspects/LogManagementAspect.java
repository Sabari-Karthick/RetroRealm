package com.batman.aspects;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogManagementAspect {

	@Pointcut(value = "execution(* com.Batman.restcontroller.*.*(..)) || execution(* com.Batman.advice.*.*(..))")
	public void executeLogs() {

	}

	@Around(value = "executeLogs()")
	public Object logsAround(ProceedingJoinPoint joinPoint) throws Throwable {
		String className = "CLASS: [" + joinPoint.getTarget().getClass().getSimpleName() + "],";
		String methodName = " METHOD: [" + joinPoint.getSignature().getName() + "()],";
		log.info("Entered" + className + "...." + methodName + ".......");
		Object response = joinPoint.proceed();

		log.info(className + methodName + " REQUEST: ");
		if (joinPoint.getArgs().length > 0) {
			Arrays.stream(joinPoint.getArgs()).forEach(args -> log.info(args.toString()));
		} else {
			log.info("[]");
		}
		log.info(className + methodName + " RESPONSE: " + response.toString());
		return response;
	}

}
