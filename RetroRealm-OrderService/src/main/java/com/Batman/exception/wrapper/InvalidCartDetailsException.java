package com.Batman.exception.wrapper;


public class InvalidCartDetailsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidCartDetailsException() {
		super();
	}
	
	public InvalidCartDetailsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidCartDetailsException(String message) {
		super(message);
	}
	
	public InvalidCartDetailsException(Throwable cause) {
		super(cause);
	}
	
	
	
}










