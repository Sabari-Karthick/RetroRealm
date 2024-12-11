package com.batman.validators;

import java.time.LocalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;

public class FutureTimeValidator implements ConstraintValidator<FutureTime, LocalTime>{

	@Override
	public boolean isValid(@NotNull LocalTime time, ConstraintValidatorContext context) {
		return !time.isBefore(LocalTime.now());
	}

}
