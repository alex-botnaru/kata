package com.kata.word.search.exception;

public class WordNotFoundException extends Exception {

	private static final long serialVersionUID = 2737417921686268935L;

	public WordNotFoundException() {
		super();
	}

	public WordNotFoundException(String message) {
		super(message);
	}
}
