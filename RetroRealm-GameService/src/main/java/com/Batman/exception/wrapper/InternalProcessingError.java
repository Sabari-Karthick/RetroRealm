package com.Batman.exception.wrapper;


public class InternalProcessingError extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InternalProcessingError() {
		super();
	}
	
	public InternalProcessingError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InternalProcessingError(String message) {
		super(message);
	}
	
	public InternalProcessingError(Throwable cause) {
		super(cause);
	}
	
	
	
}










