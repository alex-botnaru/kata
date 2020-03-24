package com.accenture.kata.word.search;

public class Coordinates {

	private int x;
	private int y;
	private int index; // letter index in the word

	public Coordinates(int x, int y, int index) {

		if (x < 0 || y < 0 || index < 0) {
			throw new IllegalArgumentException("Coordinates values must by positive");
		}

		this.x = x;
		this.y = y;
		this.index = index;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;

		if (obj instanceof Coordinates) {
			Coordinates other = (Coordinates) obj;
			isEqual = x == other.x && y == other.y;
		}

		return isEqual;
	}

	@Override
	public int hashCode() {
		return x * 1000 + y;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", x, y);
	}
}
