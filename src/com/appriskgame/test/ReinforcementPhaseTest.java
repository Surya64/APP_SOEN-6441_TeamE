package com.appriskgame.test;

import static org.junit.Assert.assertEquals;
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

	GamePlayer player1, player2, player3;
	Country country1, country2, country3, country4, country5, country6, country7, country8, country9, country10;
	Continent continent, continent1;

	/**
	 * This is the setup method for the pre-requisite values before the test cases
	 */
	@Before
	public void initializeReinforcementPhaseTest() {
		continent = new Continent();

		player1 = new GamePlayer();
		player2 = new GamePlayer();

		continent.setContinentName("Asia");
		continent.setContinentControlValue(4);

		continent1.setContinentName("Africa");
		continent.setContinentControlValue(2);

		country1 = new Country();
		country1.setCountryName("India");
		player1.getPlayerCountries().add(country1);
		player2.getPlayerCountries().add(country1);
		continent.getListOfCountries().add(country1);

		country2 = new Country();
		country2.setCountryName("China");
		player1.getPlayerCountries().add(country2);
		player2.getPlayerCountries().add(country2);
		continent.getListOfCountries().add(country2);

		country3 = new Country();
		country3.setCountryName("Srilanka");
		player1.getPlayerCountries().add(country3);
		player2.getPlayerCountries().add(country3);
//		continent.getListOfCountries().add(country3);

		country4 = new Country();
		country4.setCountryName("Sounth Africa");
		player1.getPlayerCountries().add(country4);
		player2.getPlayerCountries().add(country4);
		country5 = new Country();
		country6 = new Country();
		country7 = new Country();
		country8 = new Country();
		country9 = new Country();
		country10 = new Country();
		player2.getPlayerCountries().add(country5);
		player2.getPlayerCountries().add(country6);
		player2.getPlayerCountries().add(country7);
		player2.getPlayerCountries().add(country8);
		player2.getPlayerCountries().add(country9);
		player2.getPlayerCountries().add(country10);
//		continent.getListOfCountries().add(country4);
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
		continent.getListOfCountries().add(country3);
		assertEquals(expected, ReinforcementPhase.assignReinforcedArmies(player2, continent));
	}
}
