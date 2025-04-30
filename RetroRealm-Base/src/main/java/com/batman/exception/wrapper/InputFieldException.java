package com.batman.exception.wrapper;

import java.io.Serial;

public class InputFieldException extends RuntimeException {


	@Serial
	private static final long serialVersionUID = 1L;
	
	public InputFieldException(String message) {
		super(message);
	}
	

}
