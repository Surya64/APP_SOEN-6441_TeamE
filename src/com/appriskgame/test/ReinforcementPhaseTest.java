package com.appriskgame.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.GamePlayer;

/**
 * This Test class tests the ReinforcementPhaseTest class functinalities
 *
 * @author Shruthi
 *
 */

public class ReinforcementPhaseTest {

	/** Creating instances of player to play the game */
	GamePlayer player1, player2;

	/** Creating Objects for Country */
	Country country1, country2, country3;

	/** Creating a object Continent */
	Continent continent;

	/**
	 * This is the setup method for the pre-requisite values before the test cases
	 */
	public ReinforcementPhaseTest() {
		continent = new Continent();

		player1 = new GamePlayer();
		player2 = new GamePlayer();

		country1 = new Country();
		country2 = new Country();
		country3 = new Country();

	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}


}
