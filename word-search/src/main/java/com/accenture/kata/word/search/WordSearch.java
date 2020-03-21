package com.accenture.kata.word.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.accenture.kata.word.search.exception.InvalidGridException;
import com.accenture.kata.word.search.exception.InvalidWordException;

public class WordSearch {

	private List<Word> words;
	private List<String> rows;
	private int gridSize;

	public WordSearch(Path path) throws IOException, InvalidWordException, InvalidGridException {
		words = new ArrayList<>();
		rows = new ArrayList<>();
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
		}

		if (rows.size() != gridSize) {
			throw new InvalidGridException("The Grid has to many rows");
		}
	}

	private boolean containsNumbers(String value) {
		return value.matches(".*\\d.*");
	}

	public List<Word> getWords() {
		return words;
	}

}
