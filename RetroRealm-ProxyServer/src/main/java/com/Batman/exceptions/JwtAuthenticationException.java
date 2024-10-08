package com.Batman.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import lombok.Getter;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

	
	private static final long serialVersionUID = 1L;

	
	private final HttpStatus httpStatus;

    public JwtAuthenticationException(String msg) {
        super(msg);
        this.httpStatus = HttpStatus.UNAUTHORIZED;
    }

    public JwtAuthenticationException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}
