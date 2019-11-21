package com.appriskgame.strategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.appriskgame.controller.Player;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public class RandomPlayer implements PlayerStrategy {

	Player playerController;

	@Override
	public void placeArmies(GameMap gameMap, GamePlayer player) {
		playerController = new Player();
		Random random = new Random();
		int countryNumber = random.nextInt(player.getPlayerCountries().size());
		String countryName = player.getPlayerCountries().get(countryNumber).getCountryName();
		playerController.placearmyassigned(player, countryName);

	}

	@Override
	public void reinforcementPhase(GamePlayer player, GameMap gameMap) throws Exception {
		playerController = new Player();
		playerController.startReinforcement(player, gameMap);
		Country country = getRandomCountry(gameMap, player);
		int numOfarmies = player.getNoOfArmies();
		playerController.userAssignedArmiesToCountries(country, numOfarmies, player);
		System.out.println(country.getCountryName() + " reinforced with " + numOfarmies);
	}

	@Override
	public void attackPhase(GameMap gameMap, GamePlayer player, ArrayList<GamePlayer> playersList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fortificationPhase(GameMap gameMap, GamePlayer player) throws IOException {
		playerController = new Player();
		if (playerController.startGameFortification(player, gameMap)) {
			boolean countryFound = false;
			do {
				Country fromCountry = getRandomCountry(gameMap, player);
				Country toCountry = getRandomNeighbourCountry(fromCountry, gameMap);
				if (!fromCountry.getCountryName().equalsIgnoreCase(toCountry.getCountryName())) {
					countryFound = true;
				}
				if (countryFound) {
					int armiesCount = new Random().nextInt(fromCountry.getNoOfArmies()) + 1;
					if (armiesCount > 1 && (fromCountry.getNoOfArmies() - armiesCount) >= 1) {
						playerController.moveArmies(fromCountry, toCountry, armiesCount);
					}
				}
			} while (!countryFound);
		}
	}

	/**
	 * this method gets a random country owned by the Random player
	 * 
	 * @param mapGraph - The object of the GameMap
	 * @param player   - The object of the player
	 * @return randomCountry
	 */
	public Country getRandomCountry(GameMap mapGraph, GamePlayer player) {
		int countryIndexAssignment = new Random().nextInt(player.getPlayerCountries().size());
		return player.getPlayerCountries().get(countryIndexAssignment);
	}

	/**
	 * this method gets a random neighbor country owned by the Random player
	 * 
	 * @param gamemap     - object of the GameMap
	 * @param fromcountry - Country Object
	 * @return random neighbor Country
	 */
	public Country getRandomNeighbourCountry(Country fromcountry, GameMap gamemap) {
		Country neighbourCountry = null;
		int neighbourIndex = new Random().nextInt(fromcountry.getNeighbourCountries().size());
		String countryName = fromcountry.getNeighbourCountries().get(neighbourIndex);
		for (Country country : gamemap.getCountries()) {
			if (country.getCountryName().equalsIgnoreCase(countryName)) {
				neighbourCountry = country;
				break;
			}
		}
		return neighbourCountry;
	}

}
