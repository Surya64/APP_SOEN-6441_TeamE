package com.appriskgame.strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.controller.Player;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public class Human implements PlayerStrategy {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	Player playerController;
	public static String playersChoice;
	public static List<String> playersChoiceList = new ArrayList<String>();

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
				placeArmyFlag = !playerController.placearmyassigned(player, data[1]);
			}

		} while (placeArmyFlag);

	}

	@Override
	public void reinforcementPhase(GamePlayer player, GameMap gameMap) throws Exception {
		playerController = new Player();
		playerController.startReinforcement(player, gameMap);

		while (player.getNoOfArmies() > 0) {
			System.out.println(" Player Name :" + player.getPlayerName());
			gameMap.setActionMsg("Armies available for Reinforcement: " + player.getNoOfArmies(), "action");
			System.out.println("Armies available for Reinforcement: " + player.getNoOfArmies());
			System.out.println(
					"Please enter the country and number of armies to reinforcein the format: reinforce countryname num");
			playersChoice = br.readLine().trim();
			playersChoiceList = Arrays.asList(playersChoice.split(" "));
			if (!(playersChoiceList.size() == 3)) {
				System.out.println("Please enter the right format like : reinforce countryname num");
				playersChoice = br.readLine().trim();
				playersChoiceList = Arrays.asList(playersChoice.split(" "));
			}
			String strreinforce = playersChoiceList.get(0);
			String countryName = playersChoiceList.get(1);
			String armyCount = playersChoiceList.get(2);
			Country countryNameObject = new Country();

			// Saving the players country in an object, so that all the country details will
			// be taken further
			for (Country country : player.getPlayerCountries()) {
				if (country.getCountryName().equalsIgnoreCase(countryName)) {
					countryNameObject = country;
				}
			}
			Pattern namePattern2 = Pattern.compile("reinforce");
			Matcher match2 = namePattern2.matcher(strreinforce);
			Pattern namePattern1 = Pattern.compile("[a-zA-Z-\\s]+");
			Matcher match1 = namePattern1.matcher(countryName);
			Pattern numberPattern = Pattern.compile("[0-9]+");
			Matcher match = numberPattern.matcher(armyCount);

			while (!match.matches() || armyCount.isEmpty() || !player.getPlayerCountries().contains(countryNameObject)
					|| !match1.matches() || !match2.matches()) {
				if (!player.getPlayerCountries().contains(countryNameObject) || !match1.matches()) {
					System.out.println("Please enter the country that you own in right format");
					playersChoice = br.readLine().trim();
					playersChoiceList = Arrays.asList(playersChoice.split(" "));
					if (!(playersChoiceList.size() == 3)) {
						System.out.println("Please enter the right format like : reinforce countryname num");
						playersChoice = br.readLine().trim();
						playersChoiceList = Arrays.asList(playersChoice.split(" "));
					}
					strreinforce = playersChoiceList.get(0);
					countryName = playersChoiceList.get(1);
					armyCount = playersChoiceList.get(2);
					for (Country country : player.getPlayerCountries()) {
						if (country.getCountryName().equalsIgnoreCase(countryName)) {
							countryNameObject = country;
						}
					}

					match1 = namePattern1.matcher(countryName);
					match2 = namePattern2.matcher(strreinforce);
					match = numberPattern.matcher(armyCount);
				}

				if (!match.matches() || armyCount.isEmpty()) {
					System.out.println("\nPlease enter the correct army count in right format");
					playersChoice = br.readLine().trim();
					playersChoiceList = Arrays.asList(playersChoice.split(" "));
					if (!(playersChoiceList.size() == 3)) {
						System.out.println("Please enter the right format like : reinforce countryname num");
						playersChoice = br.readLine().trim();
						playersChoiceList = Arrays.asList(playersChoice.split(" "));
					}

					countryName = playersChoiceList.get(1);
					armyCount = playersChoiceList.get(2);
					match = numberPattern.matcher(armyCount);
					for (Country country : player.getPlayerCountries()) {
						if (country.getCountryName().equalsIgnoreCase(countryName)) {
							countryNameObject = country;
						}
					}

					match1 = namePattern1.matcher(countryName);
					match2 = namePattern2.matcher(strreinforce);
					match = numberPattern.matcher(armyCount);
				}
				if (!match2.matches()) {
					System.out.println("\nPlease enter the right format like : reinforce countryname num");
					playersChoice = br.readLine().trim();
					playersChoiceList = Arrays.asList(playersChoice.split(" "));
					if (!(playersChoiceList.size() == 3)) {
						System.out.println("Please enter the right format like : reinforce countryname num");
						playersChoice = br.readLine().trim();
						playersChoiceList = Arrays.asList(playersChoice.split(" "));
					}
					strreinforce = playersChoiceList.get(0);
					countryName = playersChoiceList.get(1);
					armyCount = playersChoiceList.get(2);

					for (Country country : player.getPlayerCountries()) {

						if (country.getCountryName().equalsIgnoreCase(countryName)) {
							countryNameObject = country;
						}

					}
					match1 = namePattern1.matcher(countryName);
					match2 = namePattern2.matcher(strreinforce);
					match = numberPattern.matcher(armyCount);
				}
			}

			int numOfarmies = Integer.parseInt(armyCount);
			for (Country country : player.getPlayerCountries()) {
				if (country.getCountryName().equalsIgnoreCase(countryName)) {
					playerController.userAssignedArmiesToCountries(country, numOfarmies, player);
				}
			}
		}
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
