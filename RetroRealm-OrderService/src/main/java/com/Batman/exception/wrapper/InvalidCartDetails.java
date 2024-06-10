package com.Batman.exception.wrapper;


public class InvalidCartDetails extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidCartDetails() {
		super();
	}
	
	public InvalidCartDetails(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidCartDetails(String message) {
		super(message);
	}
	
	public InvalidCartDetails(Throwable cause) {
		super(cause);
	}
	
	
	
}










