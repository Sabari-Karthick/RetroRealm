package com.Batman.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Batman.exception.payload.ExceptionMsg;
import com.Batman.exception.wrapper.AlreadyExistsException;
import com.Batman.exception.wrapper.CategoryNotFoundException;
import com.Batman.exception.wrapper.GameNotFoundException;
import com.Batman.exception.wrapper.GameOwnerNotFoundException;
import com.Batman.exception.wrapper.InputFieldException;
import com.Batman.exception.wrapper.TooManyRequestException;
import com.Batman.exception.wrapper.VerificationMissingException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
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
	
	@ExceptionHandler(value = { DataIntegrityViolationException.class })
	public <T extends DataAccessException> ResponseEntity<ExceptionMsg> handleDataAccessException(final T e) {

		log.info("**ApiExceptionHandler controller, handle data access exception exception*\n");
		String errorMessage = e.getMessage();
		final var badRequest = HttpStatus.BAD_REQUEST;
		if(e.getMessage().contains("Duplicate")) errorMessage = "Record Already Exists";
		return new ResponseEntity<>(ExceptionMsg.builder().msg("*" + errorMessage + "!**").httpStatus(badRequest)
				.timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), badRequest);
	}
	

	@ExceptionHandler(value = { Throwable.class })
	public ResponseEntity<ExceptionMsg> handleAllException(final Throwable e) {

		log.info("**ApiExceptionHandler controller, handle  exception*\n");
		final var internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(ExceptionMsg.builder().msg("*" + e.getMessage() + "!**")
				.httpStatus(internalServerError).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(),
				internalServerError);
	}

}
