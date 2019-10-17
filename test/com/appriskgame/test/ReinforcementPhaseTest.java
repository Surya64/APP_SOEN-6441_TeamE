package com.appriskgame.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.GamePlayer;
import com.appriskgame.services.ReinforcementPhase;

/**
 * This Test class tests the ReinforcementPhaseTest class functionalities
 *
 * @author Shruthi
 */
public class ReinforcementPhaseTest {
	private GamePlayer player1, player2;
	private Country country1, country2, country3, country4, country5, country6, country7, country8, country9, country10,
			country11, country12, country13, country14;
	private Continent continent;

	/**
	 * This is the setup method for the pre-requisite values before the test cases
	 */
	@Before
	public void initializeReinforcementPhaseTest() {
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
	}

	/**
	 * Testing whether the count of reinforcement armies is proper
	 */
	@Test
	public void testassignReinforcedArmies() {
		int expected = 3;
		assertEquals(expected, ReinforcementPhase.assignReinforcedArmies(player1, continent));
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
		assertEquals(expected, ReinforcementPhase.assignReinforcedArmies(player2, continent));
	}
}
