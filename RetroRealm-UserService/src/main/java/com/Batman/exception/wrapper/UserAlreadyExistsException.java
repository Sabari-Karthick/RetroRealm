package com.Batman.exception.wrapper;


public class UserAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public UserAlreadyExistsException() {
		super();
	}
	
	public UserAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UserAlreadyExistsException(String message) {
		super(message);
	}
	
	public UserAlreadyExistsException(Throwable cause) {
		super(cause);
	}
	
	
	
}










