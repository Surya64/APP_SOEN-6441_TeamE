package com.appriskgame.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import com.appriskgame.model.Country;
import com.appriskgame.services.FortificationPhase;

/**
 * Test class for FortificationPhase Class
 * 
 * @author Dolly
 *
 */
public class FortificationPhaseTest {
	
	/** Objects for Country Class */
	private Country country, toCountry, fromCountry, toCountry1;
	
	/** Object for Fortification Class */
	private FortificationPhase fortificationPhase;
	
	/** ArrayList for storing neighbour countries list for the countries */
	private ArrayList<String> NeighbourCountries;
	
	/**
	 * FortificationPhaseTest Constructor for initial setup 
	 */
	public FortificationPhaseTest() {
		
		fortificationPhase = new FortificationPhase();
		NeighbourCountries = new ArrayList<String>();
		
		country = new Country();
		country.setCountryName("India");
		NeighbourCountries.add("India");
		
		country = new Country();
		country.setCountryName("Nepal");
		NeighbourCountries.add("Nepal");
		
		country = new Country();
		country.setCountryName("Srilanka");
		NeighbourCountries.add("Srilanka");
		
		country = new Country();
		country.setCountryName("China");
		NeighbourCountries.add("China");
		
		fromCountry = new Country();
		fromCountry.setCountryName("Bangladesh");
		fromCountry.setNoOfArmies(8);
		
		toCountry = new Country();
		toCountry.setCountryName("India");
		toCountry.setNoOfArmies(4);
		
		toCountry1 = new Country();
		toCountry1.setCountryName("Canada");
		toCountry1.setNoOfArmies(2);
		fromCountry.setNeighbourCountries(NeighbourCountries);
		
	}
	
	/**
	 * Test method to validate the number of armies present in the fromCountry and the toCountry
	 * after the player moves the armies between the adjacent fromCountry and toCountry.
	 */
	@Test
	public void isFortificationComplete() {
		fortificationPhase.moveArmies(fromCountry, toCountry, 2);
		assertEquals(6, fromCountry.getNoOfArmies());
		assertEquals(6, toCountry.getNoOfArmies());
	}
	
	/**
	 * Test method to validate the number of armies present in the fromCountry and the toCountry
	 * after the player moves the armies between fromCountry and toCountry which are not adjacent.
	 */
	@Test 
	public void isFortificationNotComplete() {
		fortificationPhase.moveArmies(fromCountry, toCountry1, 2);
		assertEquals(8, fromCountry.getNoOfArmies());
		assertEquals(2, toCountry1.getNoOfArmies());
	}
}