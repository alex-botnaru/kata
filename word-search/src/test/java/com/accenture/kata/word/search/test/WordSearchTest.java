package com.accenture.kata.word.search.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.accenture.kata.word.search.Coordinates;
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

		assertThrows(InvalidWordException.class,
				() -> new WordSearch(getResourcePath("word-search-input-invalid-word.txt")));
		assertThrows(InvalidWordException.class,
				() -> new WordSearch(getResourcePath("word-search-input-missing-words.txt")));
		assertThrows(InvalidWordException.class,
				() -> new WordSearch(getResourcePath("word-search-input-no-words-list.txt")));
	}

	@Test
	public void whenWordSearchLoadsAFileWithAWordCotainingNumberAndThrowsAnException() {
		assertThrows(InvalidWordException.class,
				() -> new WordSearch(getResourcePath("word-search-input-invalid-word-with-number.txt")));
	}

	@Test
	public void whenWordSearchLoadsAFileWithInvalidRowsInGridAndThrowsAnException() {
		assertThrows(InvalidGridException.class,
				() -> new WordSearch(getResourcePath("word-search-input-invalid-rows-number.txt")));
	}

	@Test
	public void whenWordSearchLoadsAFileWithInvalidColumnsInGridAndThrowsAnException() {
		assertThrows(InvalidGridException.class,
				() -> new WordSearch(getResourcePath("word-search-input-invalid-columns-number.txt")));
	}

	@Test
	public void whenWordSearchLoadsAFileWithGridContainingNumbersAndThrowsAnException() {
		assertThrows(InvalidGridException.class,
				() -> new WordSearch(getResourcePath("word-search-input-grid-with-numbers.txt")));
	}

	@Test
	public void whenSearchForWordsHorizontalyForwardAndReturnTheirLocations()
			throws IOException, InvalidWordException, InvalidGridException, URISyntaxException {
		WordSearch wordSearch = new WordSearch(getResourcePath("word-search-input-valid-size6.txt"));
		Set<Coordinates> expectedLocationWordTree = new HashSet<>(Arrays.asList(new Coordinates(2, 0), new Coordinates(3, 0),
				new Coordinates(4, 0), new Coordinates(5, 0)));
		Set<Coordinates> expectedLocationWordFlower = new HashSet<>(
				Arrays.asList(new Coordinates(0, 1), new Coordinates(1, 1), new Coordinates(2, 1),
						new Coordinates(3, 1), new Coordinates(4, 1), new Coordinates(5, 1)));
		Set<Coordinates> expectedLocationWordPlant = new HashSet<>(Arrays.asList(new Coordinates(0, 5),
				new Coordinates(1, 5), new Coordinates(2, 5), new Coordinates(3, 5), new Coordinates(4, 5)));
		
		searchForWordLocationTest(wordSearch, "TREE", expectedLocationWordTree);
		searchForWordLocationTest(wordSearch, "FLOWER", expectedLocationWordFlower);
		searchForWordLocationTest(wordSearch, "PLANT", expectedLocationWordPlant);

	}
	
	private void searchForWordLocationTest(WordSearch wordSearch, String word, Set<Coordinates> expectedLocation) {
		Set<Coordinates> actualLocation = wordSearch.findLocationForWord(word);
		assertEquals(expectedLocation.size(), actualLocation.size());
		assertIterableEquals(expectedLocation, actualLocation);
	}

	private Path getResourcePath(String fileName) throws URISyntaxException {
		return Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
	}

}
