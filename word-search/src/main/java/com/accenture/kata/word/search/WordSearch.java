package com.accenture.kata.word.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WordSearch {

	List<Word> words;

	public WordSearch(Path path) throws IOException {
		words = new ArrayList<>();
		List<String> lines = Files.readAllLines(path);

		// First line should contain all the words
		String[] listOfWords = lines.get(0).split(",");
		for (String word : listOfWords) {
			words.add(new Word(word));
		}
	}

	public List<Word> getWords() {
		return words;
	}

}
