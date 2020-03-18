package com.accenture.kata.word.search.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.accenture.kata.word.search.WordSearch;
import com.accenture.kata.word.search.exception.InvalidGridException;
import com.accenture.kata.word.search.exception.InvalidWordException;

public class WordSearchTest {

	@Test
	public void whenWordSearchLoadsAValidFileAndHasAllWords()
			throws URISyntaxException, IOException, InvalidWordException, InvalidGridException {
		WordSearch wordSearch = new WordSearch(getResourcePath("word-search-input-valid-size5.txt"));
		assertNotNull(wordSearch.getWords());
		assertEquals(5, wordSearch.getWords().size());
	}

	@Test
	public void whenWordSearchLoadsAFileWithInvalidWordAndThrowsAnException() {

		assertThrows(InvalidWordException.class, () -> new WordSearch(getResourcePath("word-search-input-invalid-word.txt")));
		assertThrows(InvalidWordException.class, () -> new WordSearch(getResourcePath("word-search-input-missing-words.txt")));
		assertThrows(InvalidWordException.class, () -> new WordSearch(getResourcePath("word-search-input-no-words-list.txt")));
	}
	
	@Test
	public void whenWordSearchLoadsAFileWithAWordCotainingNumberAndThrowsAnException() {
		assertThrows(InvalidWordException.class, () -> new WordSearch(getResourcePath("word-search-input-invalid-word-with-number.txt")));
	}
	
	@Test
	public void whenWordSearchLoadsAFileWithInvalidRowsInGridAndThrowsAnException() {
		assertThrows(InvalidGridException.class, () -> new WordSearch(getResourcePath("word-search-input-invalid-rows-number.txt")));
	}
	
	@Test
	public void whenWordSearchLoadsAFileWithInvalidColumnsInGridAndThrowsAnException() {
		assertThrows(InvalidGridException.class, () -> new WordSearch(getResourcePath("word-search-input-invalid-columns-number.txt")));
	}
	
	@Test
	public void whenWordSearchLoadsAFileWithGridContainingNumbersAndThrowsAnException() {
		assertThrows(InvalidGridException.class, () -> new WordSearch(getResourcePath("word-search-input-grid-with-numbers.txt")));
	}

	private Path getResourcePath(String fileName) throws URISyntaxException {
		return Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
	}

}
