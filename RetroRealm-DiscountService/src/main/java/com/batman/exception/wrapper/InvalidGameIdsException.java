package com.batman.exception.wrapper;

import java.io.Serial;

public class InvalidGameIdsException extends RuntimeException {


	@Serial
	private static final long serialVersionUID = 1L;

	public InvalidGameIdsException(String message) {
		super(message);
	}
	

}
