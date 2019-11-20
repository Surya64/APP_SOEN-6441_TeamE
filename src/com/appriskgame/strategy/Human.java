package com.appriskgame.strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.controller.Player;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public class Human implements PlayerStrategy {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	Player playerController;

	@Override
	public void placeArmies(GameMap gameMap, GamePlayer player) throws IOException {
		playerController = new Player();
		boolean placeArmyFlag = true;
		do {
			placeArmyFlag = false;
			System.out.println("Enter Command to place Army to Country");
			String input = br.readLine().trim();
			String[] data = input.split(" ");
			Pattern commandName = Pattern.compile("placearmy");
			Matcher commandMatch = commandName.matcher(data[0]);
			if (!commandMatch.matches() || input.isEmpty()) {
				System.out.println("\nIncorrect Command");
				placeArmyFlag = true;
			}
			if (!placeArmyFlag) {
				placeArmyFlag = !playerController.placearmyassigned(player,data[1]);
			}

		} while (placeArmyFlag);

	}

	@Override
	public void reinforcementPhase(GamePlayer player, GameMap gameMap, Country country, int reinforceArmyCount) {
		// TODO Auto-generated method stub

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
