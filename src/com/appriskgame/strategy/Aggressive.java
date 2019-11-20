package com.appriskgame.strategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.appriskgame.controller.Player;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public class Aggressive implements PlayerStrategy {

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
		Country country = getStrongestCountryWithAdjCountry(gameMap, player);
		int numOfarmies = player.getNoOfArmies();
		playerController.userAssignedArmiesToCountries(country, numOfarmies, player);
		System.out.println(country.getCountryName() + " reinforced with " + numOfarmies);

	}

	@Override
	public void attackPhase(GameMap gameMap, GamePlayer player, Country attackerCountry, Country defenderCountry) {

	}

	@Override
	public void fortificationPhase(GameMap gameMap, GamePlayer player) throws IOException {
		playerController = new Player();
		Country fromCountry = null;
		int armiesCount = 0;
		if (playerController.startGameFortification(player, gameMap)) {
			int numberOfArmies = 0;
			Country strongestCountry = getStrongestCountryWithAdjCountry(gameMap, player);
			Country strongestPlayerCountry = getStrongestCountry(gameMap, player);
			if (strongestCountry.getCountryName().equalsIgnoreCase(strongestPlayerCountry.getCountryName())) {
				numberOfArmies = strongestCountry.getNoOfArmies();

				for (Country country : player.getPlayerCountries()) {
					if (country.getNoOfArmies() <= numberOfArmies && country.getNoOfArmies() > 1
							&& !country.getCountryName().equalsIgnoreCase(strongestCountry.getCountryName())) {
						numberOfArmies = country.getNoOfArmies();
						fromCountry = country;
						armiesCount = fromCountry.getNoOfArmies() - 1;
					}
				}
			} else {
				numberOfArmies = strongestCountry.getNoOfArmies();
				fromCountry = strongestPlayerCountry;
				armiesCount = (fromCountry.getNoOfArmies() - 1) / 2;
			}
			boolean fortify = false;
			for (Country country : player.getPlayerCountries()) {
				for (String temp : country.getNeighbourCountries()) {
					if (temp.equalsIgnoreCase(fromCountry.getCountryName())
							|| temp.equalsIgnoreCase(strongestCountry.getCountryName())) {
						fortify = true;
					}
				}
			}
			if (fortify) {
				if (fromCountry != null && strongestCountry != null) {
					playerController.moveArmies(fromCountry, strongestCountry, armiesCount);
				}
			} else {
				gameMap.setActionMsg("None of the players' countries are adjacent", "action");
				System.out.println("None of the players' countries are adjacent\n Fortification phase ends..!!");
			}
		}
		System.out.println("Aggressive fortification complete");

	}

	/**
	 * Method to get the strongest country from the Aggressive player's countries
	 * list
	 * 
	 * @param mapGraph - GameMapGraph object
	 * @param player   - current player which is the aggressive player
	 * @return strongestCountry - strongest country of aggressive player
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

	/**
	 * Method to get the strongest country
	 * 
	 * @param mapGraph - GameMapGraph object
	 * @param player   - current player which is the aggressive player
	 * @return strongestCountry - strongest country of aggressive player
	 */
	public Country getStrongestCountryWithAdjCountry(GameMap mapGraph, GamePlayer player) {
		int numberOfArmies = 0;
		Country strongestCountry = null;
		playerController = new Player();
		List<Country> countriesWithAdjCountries = new ArrayList<Country>();
		for (Country country : player.getPlayerCountries()) {
			for (String adjCountry : country.getNeighbourCountries()) {
				GamePlayer adjPlayer = playerController.getPlayerForCountry(mapGraph, adjCountry);
				Country adjCountryObject = playerController.getAdjacentCountry(mapGraph, adjCountry);
				if (!player.getPlayerName().equalsIgnoreCase(adjPlayer.getPlayerName())) {
					countriesWithAdjCountries.add(country);
					break;
				}
			}
		}

		for (Country country : countriesWithAdjCountries) {
			if (country.getNoOfArmies() >= numberOfArmies) {
				numberOfArmies = country.getNoOfArmies();
				strongestCountry = country;
			}
		}
		if (strongestCountry == null) {
			strongestCountry = getStrongestCountry(mapGraph, player);
		}

		return strongestCountry;
	}

}
