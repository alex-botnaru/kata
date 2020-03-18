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

		// First line should contain all the words
		String[] listOfWords = lines.get(0).split(",");
		for (String word : listOfWords) {

			if (word.length() < 2) {
				throw new InvalidWordException("A word should be minimum two letters long.");
			}

			if (word.matches(".*\\d.*")) {
				throw new InvalidWordException("A word should contain numbers.");
			}

			words.add(new Word(word));
		}
		
		// Read the  grid
		for(int i = 1; i < lines.size(); i++) {
			String row = lines.get(i).replaceAll("[ ,]", "");
			
			if(gridSize == -1) {
				gridSize = row.length();
			}
			rows.add(row);
		}
		
		if(rows.size() != gridSize) {
			throw new InvalidGridException("The Grid has to many rows");
		}
	}

	public List<Word> getWords() {
		return words;
	}

}
