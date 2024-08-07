package com.Batman.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Batman.exception.payload.ExceptionMsg;
import com.Batman.exception.wrapper.InputFieldException;
import com.Batman.exception.wrapper.NoCartAvailableException;

import feign.FeignException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler {

	@ExceptionHandler(value = { InputFieldException.class })
	public ResponseEntity<ExceptionMsg> handleValidationException(final InputFieldException e) {

		log.info("**ApiExceptionHandler controller, handle validation exception*\n");
		final var badRequest = HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(ExceptionMsg.builder().msg("*" + e.getMessage() + "!**").httpStatus(badRequest)
				.timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), badRequest);
	}

	@ExceptionHandler(value = { NoCartAvailableException.class, FeignException.class })
	public <T extends RuntimeException> ResponseEntity<ExceptionMsg> handleApiRequestException(final T e) {

		log.info("**ApiExceptionHandler controller, handle API request*\n");
		final var badRequest = HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(ExceptionMsg.builder().msg("#### " + e.getMessage() + "! ####")
				.httpStatus(badRequest).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), badRequest);
	}

	@ExceptionHandler(value = { RequestNotPermitted.class })
	public ResponseEntity<ExceptionMsg> handleTimeOutException(final Throwable e) {

		log.info("**ApiExceptionHandler controller, handle RequestNotPermitted exception*\n");
		final var requestLimitExceeded = HttpStatus.TOO_MANY_REQUESTS;

		return new ResponseEntity<>(ExceptionMsg.builder().msg("*" + e.getMessage() + "!**")
				.httpStatus(requestLimitExceeded).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(),
				requestLimitExceeded);
	}

	@ExceptionHandler(value = { Throwable.class })
	public ResponseEntity<ExceptionMsg> handleAllException(final Throwable e) {

		log.info("**ApiExceptionHandler controller, handle  exception*\n");
		final var badRequest = HttpStatus.INTERNAL_SERVER_ERROR;

		return new ResponseEntity<>(ExceptionMsg.builder().msg("*" + e.getMessage() + "!**").httpStatus(badRequest)
				.timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), badRequest);
	}

}
