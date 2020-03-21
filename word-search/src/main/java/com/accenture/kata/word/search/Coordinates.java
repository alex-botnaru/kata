package com.accenture.kata.word.search;

public class Coordinates {

	private int x;
	private int y;

	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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
		return x *1000 + y;
	}
}