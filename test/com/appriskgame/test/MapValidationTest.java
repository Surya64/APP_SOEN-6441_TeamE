package com.appriskgame.test;

import org.junit.Test;

import com.appriskgame.controller.MapValidation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.Before;

/**
 * Test Class for ReadAndWriteMap Class
 * 
 * @author Surya
 * @author Sai
 */
public class MapValidationTest {
	MapValidation mapValidation;
	private String validMap, invalidMap, notConnectedGraph;
	private String validCountryData, invalidCountryData;
	private String validContinentData, invalidContinentData;
	private String validBoundaryData, invalidBoundaryData;

	/**
	 * Initial setup for Map Validation Test.
	 */
	@Before
	public void initialize() {
		mapValidation = new MapValidation();
		validMap = "resources\\maps\\Valid1.map";
		invalidMap = "resources\\maps\\invalid2.map";
		notConnectedGraph = "resources\\maps\\testinvalidcontinent.map";
		validCountryData = "[countries]\r\n1 siberia 1 99 99\r\n2 worrick 2 99 99\r\n3 yazteck 1 99 99";
		invalidCountryData = "[countries]\r\n1 siberia\r\n2 worrick 2 99 99";
		validContinentData = "[continents]\r\nSouth-America 5 yellow\r\nameroki 10 #99NoColor";
		invalidContinentData = "[continents]\nAsia=";
		validBoundaryData = "[borders]\r\n1 2 3\r\n2 1\r\n3 1";
		invalidBoundaryData = "[borders]\nasia";
	}

	/**
	 * Test method for testing whether the map file is valid.
	 * 
	 * @throws IOException - the input output exception
	 */
	@Test
	public void testIsValidMap() throws IOException {
		assertTrue(mapValidation.validateMap(validMap));
	}

	/**
	 * Test method for testing whether the map file is invalid.
	 * 
	 * @throws IOException - the input output exception
	 */
	@Test
	public void testIsInvalidMap() throws IOException {
		assertFalse(mapValidation.validateMap(invalidMap));
	}

	/**
	 * Test method for testing whether the map connected or not.
	 * 
	 * @throws IOException - the input output exception
	 */
	@Test
	public void testnotConnectedGraph() throws IOException {
		assertFalse(mapValidation.validateMap(notConnectedGraph));
	}

	/**
	 * Test method for validating the valid country data.
	 */
	@Test
	public void testIsValidCountryData() {
		mapValidation.validateContinents(validContinentData);
		assertTrue(mapValidation.validateCountries(validCountryData));
	}

	/**
	 * Test method for validating the invalid country data.
	 */
	@Test
	public void testIsInvalidCountryData() {
		mapValidation.validateContinents(validContinentData);
		assertFalse(mapValidation.validateCountries(invalidCountryData));
	}

	/**
	 * Test method for validating the valid continent data.
	 */
	@Test
	public void testIsValidContinentData() {
		assertTrue(mapValidation.validateContinents(validContinentData));
	}

	/**
	 * Test method for validating the invalid continent data.
	 */
	@Test
	public void testIsInvalidContinentData() {
		assertFalse(mapValidation.validateContinents(invalidContinentData));
	}

	/**
	 * Test method for validating the valid border data.
	 */
	@Test
	public void testIsValidBorderData() {
		mapValidation.validateContinents(validContinentData);
		mapValidation.validateCountries(validCountryData);
		assertTrue(mapValidation.validateBoundaries(validBoundaryData));
	}

	/**
	 * Test method for validating the invalid border data.
	 */
	@Test
	public void testIsInvalidBorderData() {
		mapValidation.validateContinents(validContinentData);
		mapValidation.validateCountries(validCountryData);
		assertFalse(mapValidation.validateBoundaries(invalidBoundaryData));
	}
}
