package com.batman.exception.wrapper;

public class DiscountAlreadyExistException extends RuntimeException {


	private static final long serialVersionUID = 1L;
	
	public DiscountAlreadyExistException(String message) {
		super(message);
	}
	

}
