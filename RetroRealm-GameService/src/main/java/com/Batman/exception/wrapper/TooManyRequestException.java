package com.Batman.exception.wrapper;


public class TooManyRequestException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public TooManyRequestException() {
		super();
	}
	
	public TooManyRequestException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TooManyRequestException(String message) {
		super(message);
	}
	
	public TooManyRequestException(Throwable cause) {
		super(cause);
	}
	
	
	
}










