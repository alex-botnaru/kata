package com.accenture.kata.word.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
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
	private List<String> diagonalLeftRight;
	private List<String> diagonalRightLeft;
	private int gridSize;

	private final CoordinatesGenerator coordinatesGeneratorHorizontallyForwards = (a, i, l) -> {
		Set<Coordinates> coordinates = new HashSet<>();
		for (int x = 0; x < l; x++) {
			coordinates.add(new Coordinates(x + i, a));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorHorizontallyBackwards = (a, i, l) -> {
		Set<Coordinates> coordinates = new HashSet<>();
		for (int x = l - 1; x >= 0; x--) {
			coordinates.add(new Coordinates(x + i, a));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorVerticallyForwards = (a, i, l) -> {
		Set<Coordinates> coordinates = new HashSet<>();
		for (int y = 0; y < l; y++) {
			coordinates.add(new Coordinates(a, y + i));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorVerticallyBackwards = (a, i, l) -> {
		Set<Coordinates> coordinates = new HashSet<>();
		for (int y = l - 1; y >= 0; y--) {
			coordinates.add(new Coordinates(a, y + i));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorDiagonallyLeftRightForwards = (x, y, l) -> {
		Set<Coordinates> coordinates = new HashSet<>();
		for (int i = 0; i < l; i++) {
			coordinates.add(new Coordinates(x + i, y + i));
		}
		return coordinates;
	};

	private final CoordinatesGenerator coordinatesGeneratorDiagonallyRightLeftForwards = (x, y, l) -> {
		Set<Coordinates> coordinates = new HashSet<>();
		for (int i = 0; i < l; i++) {
			coordinates.add(new Coordinates(x + i, y - i));
		}
		return coordinates;
	};

	public WordSearch(Path path) throws IOException, InvalidWordException, InvalidGridException {
		words = new ArrayList<>();
		rows = new ArrayList<>();
		columns = new ArrayList<>();
		diagonalLeftRight = new ArrayList<>();
		diagonalRightLeft = new ArrayList<>();
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

		int dimension = gridSize * 2;
		for (int j = 0; j < dimension; j++) {
			StringBuilder diagonalLettersLeftRight = new StringBuilder();
			StringBuilder diagonalLettersRightLeft = new StringBuilder();
			for (int col = 0; col <= j; col++) {
				int row = j - col;
				int middle = gridSize - row;
				if (middle >= 0 && middle < gridSize && col < gridSize) {
					diagonalLettersLeftRight.append(rows.get(middle).charAt(col));
				}
				if (row >= 0 && row < gridSize && col < gridSize) {
					diagonalLettersRightLeft.append(rows.get(row).charAt(col));
				}
			}

			if (diagonalLettersLeftRight.length() > 0) {
				diagonalLeftRight.add(diagonalLettersLeftRight.toString());
			}
			if (diagonalLettersRightLeft.length() > 0) {
				diagonalRightLeft.add(diagonalLettersRightLeft.toString());
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
		Set<Coordinates> location = new HashSet<>();
		searchWordHorizontally(word, location);
		searchWordVertically(word, location);
		searchWrodDiagonallyLeftRight(word, location);
		searchWrodDiagonallyRightLeft(word, location);

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

	private void searchWrodDiagonallyLeftRight(String word, Set<Coordinates> location) {
		if (location.isEmpty()) {

			int x = 0;
			int y = gridSize;
			for (int i = 0; i < diagonalLeftRight.size(); i++) {
				String letters = diagonalLeftRight.get(i);
				// Search for word in a list of letters forwards and backwards
				int forwardIndex = letters.indexOf(word);
				int backwardIndex = letters.indexOf(reverseString(word));

				if (i < gridSize) {
					y--;
				} else {
					x++;
				}

				if (forwardIndex > -1) {
					location.addAll(coordinatesGeneratorDiagonallyLeftRightForwards.generate(x + forwardIndex,
							y + forwardIndex, word.length()));
				} else if (backwardIndex > -1) {
					location.addAll(coordinatesGeneratorDiagonallyLeftRightForwards.generate(x + backwardIndex,
							y + backwardIndex, word.length()));
				}

				if (!location.isEmpty()) {
					// Word is found, no need to search further
					break;
				}

			}
		}
	}

	private void searchWrodDiagonallyRightLeft(String word, Set<Coordinates> location) {
		if (location.isEmpty()) {
			int x = 0;
			int y = -1;
			for (int i = 0; i < diagonalRightLeft.size(); i++) {
				String letters = diagonalRightLeft.get(i);
				// Search for word in a list of letters forwards and backwards
				int forwardIndex = letters.indexOf(word);
				int backwardIndex = letters.indexOf(reverseString(word));
				
				if (i < gridSize) {
					y++;
				} else {
					x++;
				}
				
				if (forwardIndex > -1) {
					location.addAll(coordinatesGeneratorDiagonallyRightLeftForwards.generate(x + forwardIndex,
							y - forwardIndex, word.length()));
				} else if (backwardIndex > -1) {
					location.addAll(coordinatesGeneratorDiagonallyRightLeftForwards.generate(x + backwardIndex,
							y - backwardIndex, word.length()));
				}

				if (!location.isEmpty()) {
					// Word is found, no need to search further
					break;
				}

			}
		}
	}

	private Set<Coordinates> searchWordInListOfLetters(String word, List<String> lettersList,
			CoordinatesGenerator forward, CoordinatesGenerator backward) {
		Set<Coordinates> location = new HashSet<>();
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
