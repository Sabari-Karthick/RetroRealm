package com.batman.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureTimeValidator.class)
public @interface FutureTime {
	
	String message() default "Time is Not a future value";
	
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
