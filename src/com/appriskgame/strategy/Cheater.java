package com.appriskgame.strategy;

import java.util.ArrayList;
import java.util.Random;

import com.appriskgame.controller.Player;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public class Cheater implements PlayerStrategy {

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
	public void reinforcementPhase(GamePlayer player, GameMap gameMap) {
		playerController = new Player();
		ArrayList<Country> countryList = player.getPlayerCountries();
		for(int i=0; i < countryList.size();i++) {
			Country currentCountry = countryList.get(i);
			int reinforcementArmies= currentCountry.getNoOfArmies() * 2;
			currentCountry.setNoOfArmies(reinforcementArmies);
			System.out.println(currentCountry.getCountryName()+" reinforced with "+reinforcementArmies);
		}
		player.setNoOfArmies(0);

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

}
