package com.Batman.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Batman.exception.payload.ExceptionMsg;
import com.Batman.exceptions.JwtAuthenticationException;
import com.Batman.exceptions.UserRegistrationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApiExceptionHandler {

	@ExceptionHandler(value = { JwtAuthenticationException.class,UsernameNotFoundException.class,BadCredentialsException.class,OAuth2AuthenticationException.class})
	public <T extends AuthenticationException> ResponseEntity<ExceptionMsg> handleValidationException(final T e) {

		log.info("**ApiExceptionHandler controller, handle Authentication exception*\n");
		final var badRequest = HttpStatus.FORBIDDEN;

		return new ResponseEntity<>(
				ExceptionMsg.builder().msg("*" + e.getMessage() + "!**")
						.httpStatus(badRequest).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(),
				badRequest);
	}
//
//	@ExceptionHandler(value = { FeignException.class})
//	public <T extends RuntimeException> ResponseEntity<ExceptionMsg> handleApiRequestException(final T e) {
//
//		log.info("**ApiExceptionHandler controller, handle API request*\n");
//		final var badRequest = HttpStatus.BAD_REQUEST;
//
//		return new ResponseEntity<>(ExceptionMsg.builder().msg("#### " + e.getMessage() + "! ####")
//				.httpStatus(badRequest).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(), badRequest);
//	}
	
	@ExceptionHandler(value = { UserRegistrationException.class})
	public  ResponseEntity<ExceptionMsg> handleUserRegistrationException(final UserRegistrationException e) {

		log.info("**ApiExceptionHandler controller, handle UserRegistrationException *\n");
		log.info("Exception Class :: {}",e.getClass().getSimpleName());
		final var badRequest = HttpStatus.INTERNAL_SERVER_ERROR;

		return new ResponseEntity<>(
				ExceptionMsg.builder().msg("*" + e.getMessage() + "!**")
						.httpStatus(badRequest).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(),
				badRequest);
	}
	
	@ExceptionHandler(value = { Throwable.class})
	public  ResponseEntity<ExceptionMsg> handleAllException(final Throwable e) {

		log.info("**ApiExceptionHandler controller, handle unknown exception*\n");
		log.info("Exception Class :: {}",e.getClass().getSimpleName());
		final var badRequest = HttpStatus.INTERNAL_SERVER_ERROR;

		return new ResponseEntity<>(
				ExceptionMsg.builder().msg("*" + e.getMessage() + "!**")
						.httpStatus(badRequest).timestamp(ZonedDateTime.now(ZoneId.systemDefault())).build(),
				badRequest);
	}

}
