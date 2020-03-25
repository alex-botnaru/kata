package com.accenture.kata.word.search;

import java.util.Set;
import java.util.stream.Collectors;

public class Word {

	private String word;
	private Set<Coordinates> location;

	public Word(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}

	public void setLocation(Set<Coordinates> location) {
		this.location = location;
	}

	public Set<Coordinates> getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", word,
				location.stream().map(Coordinates::toString).collect(Collectors.joining(",")));
	}
}
