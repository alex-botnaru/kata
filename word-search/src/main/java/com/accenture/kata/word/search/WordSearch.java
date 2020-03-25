package com.accenture.kata.word.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.accenture.kata.word.search.exception.InvalidGridException;
import com.accenture.kata.word.search.exception.InvalidWordException;
import com.accenture.kata.word.search.exception.WordNotFoundException;

public class WordSearch {

	private List<Word> words;
	private List<String> rows;
	private List<String> columns;
	private List<String> diagonalAscending; // moving diagonally ascending from left bottom to right up corner
	private List<String> diagonalDescending; // moving diagonally descending from left top to right bottom corner
	private int gridSize;

	private final CoordinatesGenerator coordinatesGeneratorHorizontallyForwards = (a, i, l) -> {
		Set<Coordinates> coordinates = new TreeSet<>();
		for (int x = 0; x < l; x++) {
			coordinates.add(new Coordinates(x + i, a, x));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorHorizontallyBackwards = (a, i, l) -> {
		Set<Coordinates> coordinates = new TreeSet<>();
		for (int x = 0; x < l; x++) {
			coordinates.add(new Coordinates(l - x - 1 + i, a, x));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorVerticallyForwards = (a, i, l) -> {
		Set<Coordinates> coordinates = new TreeSet<>();
		for (int y = 0; y < l; y++) {
			coordinates.add(new Coordinates(a, y + i, y));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorVerticallyBackwards = (a, i, l) -> {
		Set<Coordinates> coordinates = new TreeSet<>();
		for (int y = 0; y < l; y++) {
			coordinates.add(new Coordinates(a, l - 1 - y + i, y));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorDiagonallyAscendingForwards = (x, y, l) -> {
		Set<Coordinates> coordinates = new TreeSet<>();
		for (int i = 0; i < l; i++) {
			coordinates.add(new Coordinates(x + i, y + i, i));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorDiagonallyAscendingBackwards = (x, y, l) -> {
		Set<Coordinates> coordinates = new TreeSet<>();
		for (int i = 0; i < l; i++) {
			coordinates.add(new Coordinates(x - i, y - i, i));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorDiagonallyDescendingForwards = (x, y, l) -> {
		Set<Coordinates> coordinates = new TreeSet<>();
		for (int i = 0; i < l; i++) {
			coordinates.add(new Coordinates(x + i, y - i, i));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorDiagonallyDescendingBackwards = (x, y, l) -> {
		Set<Coordinates> coordinates = new TreeSet<>();
		for (int i = 0; i < l; i++) {
			coordinates.add(new Coordinates(x - i, y + i, i));
		}
		return coordinates;
	};

	public WordSearch(Path path) throws IOException, InvalidWordException, InvalidGridException {
		words = new ArrayList<>();
		rows = new ArrayList<>();
		columns = new ArrayList<>();
		diagonalAscending = new ArrayList<>();
		diagonalDescending = new ArrayList<>();
		gridSize = -1;
		List<String> lines = Files.readAllLines(path);

		processListOfWords(lines.get(0));
		processGrid(lines);
	}

	private void processListOfWords(String firstLine) throws InvalidWordException {
		// First line should contain all the words
		String[] listOfWords = firstLine.split(",");

		for (String word : listOfWords) {

			if (word.length() < 2) {
				throw new InvalidWordException("A word should be minimum two letters long.");
			}

			if (containsNumbers(word)) {
				throw new InvalidWordException("A word should not contain numbers.");
			}

			words.add(new Word(word));
		}
	}

	private void processGrid(List<String> lines) throws InvalidGridException {

		List<StringBuilder> builderColumns = Stream.generate(StringBuilder::new).limit(lines.size())
				.collect(Collectors.toList());
		for (int i = 1; i < lines.size(); i++) {
			String row = lines.get(i).replaceAll("[ ,]", "");

			if (containsNumbers(row)) {
				throw new InvalidGridException("The Grid should not contain numbers.");
			}

			if (gridSize == -1) {
				// Assumes first row length (number of columns) should be the grid size, meaning
				// we expect all rows to have the same length and and same number of rows
				gridSize = row.length();
			} else if (row.length() != gridSize) {
				throw new InvalidGridException("The Grid has invalid number of columns");
			}
			rows.add(row);
			for (int chIndex = 0; chIndex < row.length(); chIndex++) {
				builderColumns.get(chIndex).append(row.charAt(chIndex));
			}
		}

		if (rows.size() != gridSize) {
			throw new InvalidGridException("The Grid has to many rows");
		}

		for (StringBuilder column : builderColumns) {
			columns.add(column.toString());
		}

		processGridDiagonally();
	}

	/**
	 * Saves each diagonal line in a separate string, both ascending and descending
	 */
	private void processGridDiagonally() {
		int dimension = gridSize * 2;
		for (int j = 0; j < dimension; j++) {
			StringBuilder diagonalLettersAscending = new StringBuilder();
			StringBuilder diagonalLettersDescending = new StringBuilder();
			for (int col = 0; col <= j; col++) {
				int row = j - col;
				int middle = gridSize - row;
				if (middle >= 0 && middle < gridSize && col < gridSize) {
					diagonalLettersAscending.append(rows.get(middle).charAt(col));
				}
				if (row >= 0 && row < gridSize && col < gridSize) {
					diagonalLettersDescending.append(rows.get(row).charAt(col));
				}
			}

			if (diagonalLettersAscending.length() > 0) {
				diagonalAscending.add(diagonalLettersAscending.toString());
			}
			if (diagonalLettersDescending.length() > 0) {
				diagonalDescending.add(diagonalLettersDescending.toString());
			}
		}
	}

	private boolean containsNumbers(String value) {
		return value.matches(".*\\d.*");
	}

	public List<Word> getWords() {
		return words;
	}

	public Set<Coordinates> findLocationForWord(String word) throws WordNotFoundException {
		Set<Coordinates> location = new TreeSet<>();
		searchWordHorizontally(word, location);
		searchWordVertically(word, location);
		searchWrodDiagonallyAscending(word, location);
		searchWrodDiagonallyDescending(word, location);

		if (location.isEmpty()) {
			throw new WordNotFoundException(String.format("Word [%s] was not found in the grid", word));
		}
		return location;
	}

	private void searchWordHorizontally(String word, Set<Coordinates> location) {
		if (location.isEmpty()) {
			location.addAll(searchWordInListOfLetters(word, rows, coordinatesGeneratorHorizontallyForwards,
					coordinatesGeneratorHorizontallyBackwards));
		}
	}

	private void searchWordVertically(String word, Set<Coordinates> location) {
		if (location.isEmpty()) {
			location.addAll(searchWordInListOfLetters(word, columns, coordinatesGeneratorVerticallyForwards,
					coordinatesGeneratorVerticallyBackwards));
		}
	}

	private void searchWrodDiagonallyAscending(String word, Set<Coordinates> location) {
		if (location.isEmpty()) {
			location.addAll(searchWordDiagonally(word, diagonalAscending,
					coordinatesGeneratorDiagonallyAscendingForwards, coordinatesGeneratorDiagonallyAscendingBackwards));
		}
	}

	private void searchWrodDiagonallyDescending(String word, Set<Coordinates> location) {
		if (location.isEmpty()) {
			location.addAll(
					searchWordDiagonally(word, diagonalDescending, coordinatesGeneratorDiagonallyDescendingForwards,
							coordinatesGeneratorDiagonallyDescendingBackwards));
		}
	}

	private Set<Coordinates> searchWordDiagonally(String word, List<String> diagonal, CoordinatesGenerator forward,
			CoordinatesGenerator backward) {
		Set<Coordinates> location = new TreeSet<>();
		boolean isAscending = diagonal.equals(diagonalAscending);
		int x = 0;
		int y = isAscending ? gridSize : -1;
		for (int i = 0; i < diagonal.size(); i++) {
			String letters = diagonal.get(i);
			// Search for word in a list of letters forwards and backwards
			int forwardIndex = letters.indexOf(word);
			int backwardIndex = letters.indexOf(reverseString(word));

			x = moveDiagonallyX(x, i);
			y = moveDiagonallyY(y, i, isAscending);

			if (forwardIndex > -1) {
				int forwardY = isAscending ? y + forwardIndex : y - forwardIndex;
				location.addAll(forward.generate(x + forwardIndex, forwardY, word.length()));
			} else if (backwardIndex > -1) {
				backwardIndex += word.length() - 1;
				int backwardY = isAscending ? y + backwardIndex : y - backwardIndex;
				location.addAll(backward.generate(x + backwardIndex, backwardY, word.length()));
			}

			if (!location.isEmpty()) {
				// Word is found, no need to search further
				break;
			}

		}

		return location;
	}

	private int moveDiagonallyX(int currentX, int diagonalNumber) {
		if (diagonalNumber >= gridSize) {
			return ++currentX;
		}
		return currentX;
	}

	private int moveDiagonallyY(int currentY, int diagonalNumber, boolean isAscending) {
		int newY = currentY;
		if (diagonalNumber < gridSize) {
			newY = isAscending ? --newY : ++newY;
		}
		return newY;
	}

	private Set<Coordinates> searchWordInListOfLetters(String word, List<String> lettersList,
			CoordinatesGenerator forward, CoordinatesGenerator backward) {
		Set<Coordinates> location = new TreeSet<>();
		for (int i = 0; i < lettersList.size(); i++) {
			String letters = lettersList.get(i);

			// Search for word in a list of letters forwards and backwards
			int forwardIndex = letters.indexOf(word);
			int backwardIndex = letters.indexOf(reverseString(word));

			// If found generate coordinates
			if (forwardIndex > -1) {
				location.addAll(forward.generate(i, forwardIndex, word.length()));
			} else if (backwardIndex > -1) {
				location.addAll(backward.generate(i, backwardIndex, word.length()));
			}

			if (!location.isEmpty()) {
				// Word is found, no need to search further
				break;
			}
		}

		return location;
	}

	private String reverseString(String value) {
		return new StringBuilder(value).reverse().toString();
	}

}
