package com.batman.exception.wrapper;

public class FailedToUpdateGameServiceException extends RuntimeException {


	private static final long serialVersionUID = 1L;
	
	public FailedToUpdateGameServiceException(String message) {
		super(message);
	}
	

}
