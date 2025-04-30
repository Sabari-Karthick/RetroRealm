package com.Batman.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.batman.exception.payload.ExceptionMsg;
import com.Batman.exception.wrapper.AlreadyExistsException;
import com.Batman.exception.wrapper.CategoryNotFoundException;
import com.Batman.exception.wrapper.GameNotFoundException;
import com.Batman.exception.wrapper.GameOwnerNotFoundException;
import com.Batman.exception.wrapper.TooManyRequestException;
import com.Batman.exception.wrapper.VerificationMissingException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler{


	@ExceptionHandler(value = { CategoryNotFoundException.class, GameNotFoundException.class,
			AlreadyExistsException.class, VerificationMissingException.class, GameOwnerNotFoundException.class })
	public <T extends RuntimeException> ResponseEntity<ExceptionMsg> handleApiRequestException(final T e) {

		log.info("**ApiExceptionHandler controller, handle API request*\n");
		final var badRequest = HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(ExceptionMsg.builder().msg("#### " + e.getMessage() + "! ####")
				.httpStatus(badRequest).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), badRequest);
	}

	@ExceptionHandler(value = { TooManyRequestException.class })
	public <T extends RuntimeException> ResponseEntity<ExceptionMsg> handleTooManyRequestException(final T e) {

		log.info("**ApiExceptionHandler controller, handle too many request exception*\n");
		final var tooManyRequests = HttpStatus.TOO_MANY_REQUESTS;
		return new ResponseEntity<>(ExceptionMsg.builder().msg("*" + e.getMessage() + "!**").httpStatus(tooManyRequests)
				.timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), tooManyRequests);
	}


	@ExceptionHandler(value = { CallNotPermittedException.class })
	public <T extends RuntimeException> ResponseEntity<ExceptionMsg> handleCallNotPermittedException(final T e) {

		log.info("**ApiExceptionHandler controller, handle call not permitted exception*\n");
		final var serviceNotAvailable = HttpStatus.SERVICE_UNAVAILABLE;
		return new ResponseEntity<>(ExceptionMsg.builder().msg("*" + e.getMessage() + "!**").httpStatus(serviceNotAvailable)
				.timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), serviceNotAvailable);
	}


}
