package com.appriskgame.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.appriskgame.controller.Player;
import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

/**
 * Test Class for Player class Method of startup ,reinforcement and
 * fortification
 * 
 * @author Sahana
 * @author Shruthi
 * @author Dolly
 */
public class PlayerTest {
	private GamePlayer player1, player2;
	private Country country1, country2, country3, country4, country5, country6, country7, country8, country9, country10,
			country11, country12, country13, country14;
	private Continent continent;

	private Country country, toCountry, fromCountry, toCountry1, countryStart;
	private ArrayList<String> NeighbourCountries;

	Player player;
	GameMap gameMap;
	HashMap<String, Country> countrySet;

	/**
	 * This is the setup method for the pre-requisite values before the test cases
	 */
	@Before
	public void initializePlayerTest() {
		player = new Player();
		continent = new Continent();
		player1 = new GamePlayer();
		player2 = new GamePlayer();

		country1 = new Country();
		country1.setCountryName("Egypt");
		player1.getPlayerCountries().add(country1);
		player2.getPlayerCountries().add(country1);
		continent.getListOfCountries().add(country1);

		country2 = new Country();
		country2.setCountryName("Libya");
		player1.getPlayerCountries().add(country2);
		player2.getPlayerCountries().add(country2);
		continent.getListOfCountries().add(country2);

		country3 = new Country();
		country3.setCountryName("Morocco");
		player1.getPlayerCountries().add(country3);
		continent.getListOfCountries().add(country3);

		continent.setContinentName("Northern Africa");
		continent.setContinentControlValue(4);

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

		countrySet = new HashMap<String, Country>();
		gameMap = new GameMap();
		player.getPlayerList().add(player1);
		player.getPlayerList().add(player2);
		countryStart = new Country();
		countryStart.setCountryName("China");
		countryStart.setCountryName("India");
		countrySet.put(countryStart.getCountryName(), countryStart);
		gameMap.setCountrySet(countrySet);
	}

	/**
	 * This method to validate the allocation of armies to each player as per the
	 * number of players.
	 */
	@Test
	public void testDefaultArmiesToPlayer() {
		player.defaultArmiesToPlayer();
		assertEquals(40, player1.getNoOfArmies());
	}

	/**
	 * This method to validate the populate countries allocation to each player.
	 */
	@Ignore
	public void testpopulateCountries() {
		player.populateCountries(gameMap);
		assertEquals(1, player2.getPlayerCountries().size());
	}

	/**
	 * Testing whether the count of reinforcement armies is proper
	 */
	@Test
	public void testassignReinforcedArmies() {
		int expected = 3;
		assertEquals(expected, player.assignReinforcedArmies(player1, continent));
	}

	/**
	 * This method is used to test the count of reinforcement armies
	 */
	@Test
	public void testassignReinforcedArmies2() {
		int expected = 4;
		player2.getPlayerCountries().add(country4);
		player2.getPlayerCountries().add(country5);
		player2.getPlayerCountries().add(country6);
		player2.getPlayerCountries().add(country7);
		player2.getPlayerCountries().add(country8);
		player2.getPlayerCountries().add(country9);
		player2.getPlayerCountries().add(country10);
		player2.getPlayerCountries().add(country11);
		player2.getPlayerCountries().add(country12);
		player2.getPlayerCountries().add(country13);
		player2.getPlayerCountries().add(country14);
		assertEquals(expected, player.assignReinforcedArmies(player2, continent));
	}

	/**
	 * Test method to validate the number of armies present in the fromCountry and
	 * the toCountry after the player moves the armies between the adjacent
	 * fromCountry and toCountry.
	 */
	@Test
	public void isFortificationComplete() {
		player.moveArmies(fromCountry, toCountry, 2);
		assertEquals(6, fromCountry.getNoOfArmies());
		assertEquals(6, toCountry.getNoOfArmies());
	}

	/**
	 * Test method to validate the number of armies present in the fromCountry and
	 * the toCountry after the player moves the armies between fromCountry and
	 * toCountry which are not adjacent.
	 */
	@Test
	public void isFortificationNotComplete() {
		player.moveArmies(fromCountry, toCountry1, 2);
		assertEquals(8, fromCountry.getNoOfArmies());
		assertEquals(2, toCountry1.getNoOfArmies());
	}

	/**
	 * This method to validate the initial allocation of armies to each country
	 * owned by players.
	 */
	@Test
	public void testIntialArmyAllocation() {
		player.initialArmyAllocation(gameMap);
		assertEquals(1, countryStart.getNoOfArmies());
	}

}
