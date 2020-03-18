package com.accenture.kata.word.search.exception;

public class InvalidWordException extends Exception {

	private static final long serialVersionUID = 5038599014325201664L;

	public InvalidWordException() {
		super();
	}
	
	public InvalidWordException(String message) {
		super(message);
	}
}
