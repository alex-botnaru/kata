package com.accenture.kata.word.search;

import java.util.Set;

@FunctionalInterface
public interface CoordinatesGenerator {

	public Set<Coordinates> generate(int axis, int index, int lenght);
}
