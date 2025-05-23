package com.batman.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.CompletionException;

import com.batman.exception.wrapper.InvalidGameIdsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.batman.exception.payload.ExceptionMsg;
import com.batman.exception.wrapper.DiscountAlreadyExistException;
import com.batman.exception.wrapper.FailedToUpdateGameServiceException;
import com.batman.exception.wrapper.InputFieldException;
import com.batman.exception.wrapper.NoDiscountAvailableException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler {

	@ExceptionHandler(value = { InputFieldException.class})
	public  ResponseEntity<ExceptionMsg> handleValidationException(final InputFieldException e) {

		log.info("**ApiExceptionHandler controller, handle validation exception *");
		final var badRequest = HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(
				ExceptionMsg.builder().msg("*" + e.getMessage() + "!**")
						.httpStatus(badRequest).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(),
				badRequest);
	}

	@ExceptionHandler(value = {NoDiscountAvailableException.class,FailedToUpdateGameServiceException.class,DiscountAlreadyExistException.class, InvalidGameIdsException.class})
	public <T extends RuntimeException> ResponseEntity<ExceptionMsg> handleApiRequestException(final T e) {

		log.info("**ApiExceptionHandler controller, handle API request*\n");
		final var badRequest = HttpStatus.BAD_REQUEST;
		return new ResponseEntity<>(ExceptionMsg.builder().msg("#### " + e.getMessage() + "! ####")
				.httpStatus(badRequest).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), badRequest);
	}
	
	@ExceptionHandler(value = CompletionException.class)
	public <T extends RuntimeException> ResponseEntity<ExceptionMsg> handleCompletionException(final T e) {

		log.info("**ApiExceptionHandler controller, handle Completion future exception*\n");
		final var serverError = HttpStatus.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(ExceptionMsg.builder().msg("#### " + e.getMessage() + "! ####")
				.httpStatus(serverError).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), serverError);
	}
	
	@ExceptionHandler(value = { Throwable.class})
	public  ResponseEntity<ExceptionMsg> handleAllException(final Throwable e) {

		log.info("**ApiExceptionHandler controller, handle validation exception*\n");
		final var serverError = HttpStatus.INTERNAL_SERVER_ERROR;

		return new ResponseEntity<>(
				ExceptionMsg.builder().msg("*" + e.getMessage() + "!**")
						.httpStatus(serverError).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(),
						serverError);
	}

}
