package com.appriskgame.strategy;

import java.util.Random;

import com.appriskgame.controller.Player;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public class Benevolent implements PlayerStrategy {

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
		Country country = getWeakestCountry(gameMap, player);
		int numOfarmies = player.getNoOfArmies();
		playerController.userAssignedArmiesToCountries(country, numOfarmies, player);
		System.out.println(country.getCountryName() + " reinforced with " + numOfarmies);
	}

	@Override
	public void attackPhase(GameMap gameMap, GamePlayer player, Country attackerCountry, Country defenderCountry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fortificationPhase(GameMap gameMap, GamePlayer player, Country fromCountry, Country toCountry,
			int armiesCount) {
		// TODO Auto-generated method stub

	}

	/**
	 * this method gets the weakest country owned by the benevolent player
	 * 
	 * @param mapGraph - The object of the GameMapGraph
	 * @param player   - The object of the player
	 * @return weakest country
	 */
	public Country getWeakestCountry(GameMap mapGraph, GamePlayer player) {
		int numberOfArmies = 0;
		Country weakestCountry = null;
		numberOfArmies = player.getPlayerCountries().get(0).getNoOfArmies();
		for (Country country : player.getPlayerCountries()) {
			if (country.getNoOfArmies() <= numberOfArmies) {
				numberOfArmies = country.getNoOfArmies();
				weakestCountry = country;
			}
		}
		return weakestCountry;
	}

}
