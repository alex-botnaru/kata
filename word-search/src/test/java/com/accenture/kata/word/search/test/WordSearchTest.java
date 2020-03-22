package com.accenture.kata.word.search.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.accenture.kata.word.search.Coordinates;
import com.accenture.kata.word.search.WordSearch;
import com.accenture.kata.word.search.exception.InvalidGridException;
import com.accenture.kata.word.search.exception.InvalidWordException;
import com.accenture.kata.word.search.exception.WordNotFoundException;

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
	public void whenSearchForWordsHorizontallyForwardAndReturnTheirLocations()
			throws IOException, InvalidWordException, InvalidGridException, URISyntaxException, WordNotFoundException {
		WordSearch wordSearch = new WordSearch(getResourcePath("word-search-input-valid-size6.txt"));
		searchForWordLocationTest(wordSearch, "TREE", getCoordinates(new int[][]{{2, 0}, {3, 0}, {4, 0}, {5, 0}}));
		searchForWordLocationTest(wordSearch, "FLOWER", getCoordinates(new int[][]{{0, 1}, {1, 1}, {2, 1}, {3, 1}, {4, 1}, {5, 1}}));
		searchForWordLocationTest(wordSearch, "PLANT", getCoordinates(new int[][]{{0, 5}, {1, 5}, {2, 5}, {3, 5}, {4, 5}}));

	}
	
	@Test
	public void whenSearchForNonExistingWordAndThrowsAnExeption() throws IOException, InvalidWordException, InvalidGridException, URISyntaxException {
		WordSearch wordSearch = new WordSearch(getResourcePath("word-search-input-valid-size6.txt"));
		assertThrows(WordNotFoundException.class, () -> wordSearch.findLocationForWord("BAD"));
	}
	
	@Test
	public void whenSearchForWordsHorizontallyBackwardsAndReturnTheirLocations() throws IOException, InvalidWordException, InvalidGridException, URISyntaxException, WordNotFoundException {
		WordSearch wordSearch = new WordSearch(getResourcePath("word-search-input-valid-size6.txt"));
		searchForWordLocationTest(wordSearch, "SUN", getCoordinates(new int[][]{{2, 2}, {1, 2}, {0, 2}}));
		searchForWordLocationTest(wordSearch, "BIKE", getCoordinates(new int[][]{{3, 3}, {2, 3}, {1, 3}, {0, 3}}));
		searchForWordLocationTest(wordSearch, "LAPTOP", getCoordinates(new int[][]{{5, 4}, {4, 4}, {3, 4}, {2, 4}, {1, 4}, {0, 4}}));
	}
	
	@Test
	public void whenCreatingCoordinatesObjectsWithPositiveAndNegativeValues() {
		assertDoesNotThrow(() -> new Coordinates(0, 0));
		assertDoesNotThrow(() -> new Coordinates(4, 0));
		assertDoesNotThrow(() -> new Coordinates(0, 8));
		assertDoesNotThrow(() -> new Coordinates(77, 22));
		assertThrows(IllegalArgumentException.class, () -> new Coordinates(-1, 4));
		assertThrows(IllegalArgumentException.class, () -> new Coordinates(2, -10));
		assertThrows(IllegalArgumentException.class, () -> new Coordinates(-3, -4));
	}
	
	@Test
	public void whenSearchForWordsVerticallyUpDownAndReturnTheirLocations() throws IOException, InvalidWordException, InvalidGridException, URISyntaxException, WordNotFoundException {
		WordSearch wordSearch = new WordSearch(getResourcePath("word-search-input-valid-size6.txt"));
		// The words below don't exist in the word list, but for sake of testing, search for group of letters vertically from up down
		searchForWordLocationTest(wordSearch, "DOL", getCoordinates(new int[][]{{5, 2}, {5, 3}, {5, 4}}));
		searchForWordLocationTest(wordSearch, "UKOL", getCoordinates(new int[][]{{1, 2}, {1, 3}, {1, 4}, {1, 5}}));
		searchForWordLocationTest(wordSearch, "TOSITA", getCoordinates(new int[][]{{2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}}));
	}
	
	@Test
	public void whenSearchForWordsVerticallyDownUpAndReturnTheirLocations() throws IOException, InvalidWordException, InvalidGridException, URISyntaxException, WordNotFoundException {
		WordSearch wordSearch = new WordSearch(getResourcePath("word-search-input-valid-size6.txt"));
		// The words below don't exist in the word list, but for sake of testing, search for group of letters vertically from down up
		searchForWordLocationTest(wordSearch, "PEN", getCoordinates(new int[][]{{0, 4}, {0, 3}, {0, 2}}));
		searchForWordLocationTest(wordSearch, "ODRE", getCoordinates(new int[][]{{5, 3}, {5, 2}, {5, 1}, {5, 0}}));
		searchForWordLocationTest(wordSearch, "ATISOT", getCoordinates(new int[][]{{2, 5}, {2, 4}, {2, 3}, {2, 2}, {2, 1}, {2, 0}}));
	}
	
	@Test
	public void whenSearchForWordsDiagonallyLeftRightForwardsAndReturnsTheirLocation() throws IOException, InvalidWordException, InvalidGridException, URISyntaxException, WordNotFoundException {
		WordSearch wordSearch = new WordSearch(getResourcePath("word-search-input-valid-size8.txt"));
		searchForWordLocationTest(wordSearch, "INK", getCoordinates(new int[][]{{0, 1}, {1, 2}, {2, 3}}));
		searchForWordLocationTest(wordSearch, "GROUP", getCoordinates(new int[][]{{0, 2}, {1, 3}, {2, 4}, {3, 5}, {4, 6}}));
		searchForWordLocationTest(wordSearch, "TOE", getCoordinates(new int[][]{{0, 4}, {1, 5}, {2, 6}}));

	}

	/**
	 * Helper method that returns a set of coordinates based on input
	 * two-dimensional array
	 * 
	 * @param location
	 * @return
	 */
	private Set<Coordinates> getCoordinates(int[][] location) {
		Set<Coordinates> set = new HashSet<>();
		for (int[] coordinates : location) {
			set.add(new Coordinates(coordinates[0], coordinates[1]));
		}
		return set;
	}
	
	/**
	 * Helper method that search for a word and compares the expected location
	 * 
	 * @param wordSearch
	 * @param word
	 * @param expectedLocation
	 * @throws WordNotFoundException
	 */
	private void searchForWordLocationTest(WordSearch wordSearch, String word, Set<Coordinates> expectedLocation)
			throws WordNotFoundException {
		Set<Coordinates> actualLocation = wordSearch.findLocationForWord(word);
		assertEquals(expectedLocation.size(), actualLocation.size());
		assertIterableEquals(expectedLocation, actualLocation);
	}

	private Path getResourcePath(String fileName) throws URISyntaxException {
		return Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
	}

}
