package com.kata.word.search.impl;

import java.util.Set;

@FunctionalInterface
public interface CoordinatesGenerator {

	public Set<Coordinates> generate(int axis, int index, int lenght);
}
