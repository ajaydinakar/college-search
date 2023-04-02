package com.ajay.collegesearch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoDetailsFoundException extends RuntimeException {

	public NoDetailsFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	

}
