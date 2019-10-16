package com.appriskgame.test;

import org.junit.Test;
import org.junit.Before;
import com.appriskgame.model.Continent;
import com.appriskgame.services.MapOperations;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;

/**
 * Test class for MapOperation class
 * 
 * @author Sahana
 * @author Sai
 */
public class MapOperationTest {

	private Continent continent1, continent2, continent3;
	private ArrayList<Continent> listofContinents;
	private MapOperations mapOperations;

	/**
	 * Initial setup for Map operations.
	 */
	@Before
	public void intialize() {
		listofContinents = new ArrayList<Continent>();
		mapOperations = new MapOperations();
		continent1 = new Continent();
		continent1.setContinentName("Asia");
		listofContinents.add(continent1);
		continent2 = new Continent();
		continent2.setContinentName("Africa");
		listofContinents.add(continent2);
		continent3 = new Continent();
		continent3.setContinentName("North America");
		listofContinents.add(continent3);
	}

	/**
	 * Test method for checking whether the continent is already defined in the
	 * continent list
	 */
	@Test
	public void testIsContinentUnique() {
		assertTrue(!mapOperations.doesContinentExit("North America"));
	}

	/**
	 * Test method for checking if the continent is not already defined in the list
	 */
	@Test
	public void testIsContinentNotUnique() {
		assertFalse(mapOperations.doesContinentExit("Europe"));
	}

	/**
	 * Test method for checking if the country is not already defined in the list
	 */
	@Test
	public void testCountyExist() {
		assertFalse(mapOperations.doesCountryExit("India"));
	}
}
