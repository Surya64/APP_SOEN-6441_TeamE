package com.appriskgame.strategy;

import java.io.IOException;
import java.util.ArrayList;
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
	public void attackPhase(GameMap gameMap, GamePlayer player, ArrayList<GamePlayer> playersList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fortificationPhase(GameMap gameMap, GamePlayer player) throws IOException {
		playerController = new Player();
		if (playerController.startGameFortification(player, gameMap)) {
			Country weakestCountry = getWeakestCountry(gameMap, player);
			Country strongestNeighCountryToFortify = null;
			int armycount = 0;
			for (String neighbourCountryName : weakestCountry.getNeighbourCountries()) {
				for (Country country : player.getPlayerCountries()) {
					if (country.getCountryName().equalsIgnoreCase(neighbourCountryName)) {
						if (country.getNoOfArmies() > armycount) {
							strongestNeighCountryToFortify = country;
							armycount = country.getNoOfArmies();
						}
					}
				}
			}
			if (strongestNeighCountryToFortify != null) {
				int fortifyArmiesToWeakestCountry = (strongestNeighCountryToFortify.getNoOfArmies()) / 2;
				playerController.moveArmies(strongestNeighCountryToFortify, weakestCountry,
						fortifyArmiesToWeakestCountry);
			}
		}
		System.out.println("Benevolent fortification complete");

	}

	/**
	 * this method gets the weakest country owned by the benevolent player
	 * 
	 * @param mapGraph - The object of the GameMap
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

	/**
	 * this method gets the strongest country owned by the benevolent player
	 * 
	 * @param mapGraph - The object of the GameMap
	 * @param player   - The object of the player
	 * @return strongest country
	 */
	public Country getStrongestCountry(GameMap mapGraph, GamePlayer player) {
		int numberOfArmies = 0;
		Country strongestCountry = null;
		for (Country country : player.getPlayerCountries()) {
			if (country.getNoOfArmies() >= numberOfArmies) {
				numberOfArmies = country.getNoOfArmies();
				strongestCountry = country;
			}
		}
		return strongestCountry;
	}

}
