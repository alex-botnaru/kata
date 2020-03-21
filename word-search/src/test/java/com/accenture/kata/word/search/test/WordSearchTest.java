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
import java.util.Arrays;
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
	
	@Test
	public void whenSearchForNonExistingWordAndThrowsAnExeption() throws IOException, InvalidWordException, InvalidGridException, URISyntaxException {
		WordSearch wordSearch = new WordSearch(getResourcePath("word-search-input-valid-size6.txt"));
		assertThrows(WordNotFoundException.class, () -> wordSearch.findLocationForWord("BAD"));
	}
	
	@Test
	public void whenSearchForWordsHorizontallyBackwardsAndReturnTheirLocations() throws IOException, InvalidWordException, InvalidGridException, URISyntaxException, WordNotFoundException {
		WordSearch wordSearch = new WordSearch(getResourcePath("word-search-input-valid-size6.txt"));
		Set<Coordinates> expectedLocationWordSun = new HashSet<>(Arrays.asList(new Coordinates(2, 2), new Coordinates(1, 2), new Coordinates(0, 2)));
		Set<Coordinates> expectedLocationWordBike = new HashSet<>(Arrays.asList(new Coordinates(3, 3), new Coordinates(2, 3), new Coordinates(1, 3), new Coordinates(0, 3)));
		Set<Coordinates> expectedLocationWordLaptop= new HashSet<>(Arrays.asList(new Coordinates(5, 4), new Coordinates(4, 4), new Coordinates(3, 4),new Coordinates(2, 4), new Coordinates(1, 4), new Coordinates(0, 4)));
		
		searchForWordLocationTest(wordSearch, "SUN", expectedLocationWordSun);
		searchForWordLocationTest(wordSearch, "BIKE", expectedLocationWordBike);
		searchForWordLocationTest(wordSearch, "LAPTOP", expectedLocationWordLaptop);
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
		Set<Coordinates> expectedLocationWordDol = new HashSet<>(Arrays.asList(new Coordinates(5, 2), new Coordinates(5, 3), new Coordinates(5, 4)));
		Set<Coordinates> expectedLocationWordUkol = new HashSet<>(Arrays.asList(new Coordinates(1, 2), new Coordinates(1, 3), new Coordinates(1, 4), new Coordinates(1, 5)));
		Set<Coordinates> expectedLocationWordTosita= new HashSet<>(Arrays.asList(new Coordinates(2, 0), new Coordinates(2, 1), new Coordinates(2, 2),new Coordinates(2, 3), new Coordinates(2, 4), new Coordinates(2, 5)));
		
		searchForWordLocationTest(wordSearch, "DOL", expectedLocationWordDol);
		searchForWordLocationTest(wordSearch, "UKOL", expectedLocationWordUkol);
		searchForWordLocationTest(wordSearch, "TOSITA", expectedLocationWordTosita);
	}
	
	
	// vertically horizontally  
	private void searchForWordLocationTest(WordSearch wordSearch, String word, Set<Coordinates> expectedLocation) throws WordNotFoundException {
		Set<Coordinates> actualLocation = wordSearch.findLocationForWord(word);
		assertEquals(expectedLocation.size(), actualLocation.size());
		assertIterableEquals(expectedLocation, actualLocation);
	}

	private Path getResourcePath(String fileName) throws URISyntaxException {
		return Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
	}

}
