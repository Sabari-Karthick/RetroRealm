package com.Batman.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class UserRegistrationException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;

	
	private final HttpStatus httpStatus;

    public UserRegistrationException(String msg) {
        super(msg);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public UserRegistrationException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}
