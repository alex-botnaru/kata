package com.accenture.kata.word.search.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.accenture.kata.word.search.WordSearch;

public class WordSearchTest {
	
	@Test
	public void whenWordSearchLoadsAValidFileAndHasAllWords() throws URISyntaxException, IOException {
		Path path = Paths.get(getClass().getClassLoader().getResource("word-search-valid-input-size5.txt").toURI());
		WordSearch wordSearch = new WordSearch(path);
		assertNotNull(wordSearch.getWords());
		assertEquals(5, wordSearch.getWords().size());
	}

}
