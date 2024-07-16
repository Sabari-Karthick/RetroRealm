package com.batman.exception.wrapper;

public class NoDiscountAvailableException extends RuntimeException {


	private static final long serialVersionUID = 1L;
	
	public NoDiscountAvailableException(String message) {
		super(message);
	}
	

}
