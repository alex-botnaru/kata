package com.accenture.kata.word.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.accenture.kata.word.search.exception.InvalidWordException;

public class WordSearch {

	List<Word> words;

	public WordSearch(Path path) throws IOException, InvalidWordException {
		words = new ArrayList<>();
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
	}

	public List<Word> getWords() {
		return words;
	}

}
