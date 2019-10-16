package com.appriskgame.test;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;
import com.appriskgame.services.StartupPhase;

/**
 * Test Class for StartUpPhase class
 * 
 * @author Sahana
 *
 */
public class StartupPhaseTest {
	GamePlayer player1, player2;
	StartupPhase startUpPhase;
	Country country;
	GameMap gameMap;
	HashMap<String, Country> countrySet;

	/**
	 * Initial setup for Startup Phase.
	 */
	@Before
	public void initialize() throws Exception {
		countrySet = new HashMap<String, Country>();
		gameMap = new GameMap();
		startUpPhase = new StartupPhase();
		player1 = new GamePlayer();
		player2 = new GamePlayer();
		startUpPhase.getPlayerList().add(player1);
		startUpPhase.getPlayerList().add(player2);
		country = new Country();
		country.setCountryName("China");
		country.setCountryName("India");
		countrySet.put(country.getCountryName(), country);
		gameMap.setCountrySet(countrySet);
	}

	/**
	 * This method to validate the allocation of armies to each player as per the
	 * number of players.
	 */
	@Test
	public void testDefaultArmiesToPlayer() {
		startUpPhase.defaultArmiesToPlayer();
		assertEquals(40, player1.getNoOfArmies());
	}

	/**
	 * This method to validate the initial allocation of armies to each country
	 * owned by players.
	 */
	@Test
	public void testIntialArmyAllocation() {
		startUpPhase.initialArmyAllocation(gameMap);
		assertEquals(1, country.getNoOfArmies());
	}

	/**
	 * This method to validate the populate countries allocation to each player.
	 */
	@Test
	public void testpopulateCountries() {
		startUpPhase.populateCountries(gameMap);
		assertEquals(1, player1.getPlayerCountries().size());
	}
}
