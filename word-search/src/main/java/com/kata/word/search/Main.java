package com.kata.word.search;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.kata.word.search.exception.InvalidGridException;
import com.kata.word.search.exception.InvalidWordException;
import com.kata.word.search.exception.WordNotFoundException;
import com.kata.word.search.impl.WordSearch;

public class Main {

	public static void main(String[] args) {

		if (args.length > 0) {

			Path path = Paths.get(args[0]);
			try {
				WordSearch wordSearch = new WordSearch(path);
				System.out.println(wordSearch.print());
			} catch (IOException | InvalidWordException | InvalidGridException | WordNotFoundException e) {
				System.out.println("ERROR: " + e.getMessage());
			}

		} else {
			System.out.println("Please provide a path to a valid Word Search input file.");
		}
	}
}
