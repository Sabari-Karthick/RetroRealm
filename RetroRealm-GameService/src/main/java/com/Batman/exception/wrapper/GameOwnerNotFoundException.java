package com.Batman.exception.wrapper;


public class GameOwnerNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public GameOwnerNotFoundException() {
		super();
	}
	
	public GameOwnerNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public GameOwnerNotFoundException(String message) {
		super(message);
	}
	
	public GameOwnerNotFoundException(Throwable cause) {
		super(cause);
	}
	
	
	
}










