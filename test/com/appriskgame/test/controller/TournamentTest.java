package com.appriskgame.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.appriskgame.controller.Tournament;

/**
 * Tournament class for Player class Method of startup ,reinforcement and
 * fortification
 * 
 * @author dollymodha
 * @author sai
 *
 */
public class TournamentTest {
	Tournament tournament;
	String[] tournamentList, invalidtournamentList;
	int gameNumber;
	String mapName, invalidMapName, wrongMapName;
	String invalidStrategy = "Invalid Strategy";
	String incorrectMap = "Incorrect Map";
	String fileNotFound = "File Not Found";
	ArrayList<String> playerStrategies = null;
	int turns;

	/**
	 * This method initializes the data.
	 */
	@Before
	public void initializeData() {

		tournamentList = new String[] { "tournament", "-M", "demofinal,amerokiConquest,amerokiDomination", "-P",
				"aggressive,benevolent,random", "-G", "5", "-D", "10" };
		tournament = new Tournament();
		invalidtournamentList = new String[] { "tournament", "-M", "demofinal,amerokiConquest,amerokiDomination", "-P",
				"aggressive,benevolent,random", "-G", "55", "-D", "10" };

		gameNumber = 0;
		mapName = "demofinal";
		invalidMapName = "inValid1Conquest";
		wrongMapName = "sss";
		playerStrategies = new ArrayList<String>(Arrays.asList("aggressive-aggressive", "ssd-ssd", "random-random"));
		turns = 50;
	}

	/**
	 * This method is used to test invalid strategy.
	 * 
	 * @throws Exception input/output exception
	 */
	@Test
	public void testinValidateStrategy() throws Exception {

		assertEquals(invalidStrategy, tournament.startGame(gameNumber, mapName, playerStrategies, turns));
	}

	/**
	 * This method is used to test file not found.
	 * 
	 * @throws Exception input/output exception
	 */
	@Test
	public void testFileNotFound() throws Exception {

		assertEquals(fileNotFound, tournament.startGame(gameNumber, wrongMapName, playerStrategies, turns));
	}

	/**
	 * This method is used to test validate command.
	 */
	@Test
	public void testValidateCommand() {

		assertTrue(tournament.validateCommand(tournamentList));
	}

	/**
	 * This method is used to test invalid command.
	 */
	@Test
	public void testinValidateCommand() {

		assertFalse(tournament.validateCommand(invalidtournamentList));
	}
}
