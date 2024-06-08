package com.Batman.exception.wrapper;


public class NoCartAvailableException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public NoCartAvailableException() {
		super();
	}
	
	public NoCartAvailableException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoCartAvailableException(String message) {
		super(message);
	}
	
	public NoCartAvailableException(Throwable cause) {
		super(cause);
	}
	
	
	
}










