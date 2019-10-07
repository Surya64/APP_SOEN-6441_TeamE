package com.appriskgame.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public class StartupPhase {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	ArrayList<GamePlayer> playersList = new ArrayList<GamePlayer>();
	ArrayList<String> playerNames;

	public void gamePlay(GameMap mapGraph) throws Exception {
		boolean proceed = false, populateFlag = false;

		do {
			boolean flag;
			playerNames = new ArrayList<String>();
			do {
				flag = false;
				System.out.println("Enter Player Name");
				String input = br.readLine().trim();
				if (input.contains("-add")) {
					String[] command = input.split("-add ");
					Pattern commandPattern = Pattern.compile("[a-zA-z]+ -[a-zA-z\\s-]*");
					Pattern commandName = Pattern.compile("gameplayer");
					Matcher commandMatch = commandPattern.matcher(input);
					Matcher commandNameMatch = commandName.matcher(command[0].trim());
					if (!commandMatch.matches() || input.isEmpty() || !commandNameMatch.matches()) {
						System.out.println("\nIncorrect Command");
						flag = true;
					} else {
						for (int i = 1; i < command.length; i++) {
							String name = command[i].trim();
							Pattern namePattern = Pattern.compile("[a-zA-z]+");
							Matcher match = namePattern.matcher(name);
							if (!match.matches() || name.isEmpty()) {
								System.out.println("\nPlease enter the correct player name");
								flag = true;
							}
							playerNames.add(name);
						}
					}
				} else if (input.contains("-remove")) {
					String[] command = input.split("-remove ");
					Pattern commandPattern = Pattern.compile("[a-zA-z]+ -[a-zA-z\\s-]*");
					Pattern commandName = Pattern.compile("gameplayer");
					Matcher commandMatch = commandPattern.matcher(input);
					Matcher commandNameMatch = commandName.matcher(command[0].trim());
					if (!commandMatch.matches() || input.isEmpty() || !commandNameMatch.matches()) {
						System.out.println("\nIncorrect Command");
						flag = true;
					} else {
						for (int i = 1; i < command.length; i++) {
							String name = command[i].trim();
							Pattern namePattern = Pattern.compile("[a-zA-z]+");
							Matcher match = namePattern.matcher(name);
							if (!match.matches() || name.isEmpty()) {
								System.out.println("\nPlease enter the correct player name");
								flag = true;
							}
							if (playerNames.contains(name)) {
								playerNames.remove(name);
							} else {
								System.out.println(name + "doesn't exist");
							}
						}
					}

				}
			} while (flag);
			if (playerNames.size() > 5 || playerNames.size() < 2) {
				System.out.println("Sorry! The numbers of players can be between 2 and 6.");
				proceed = true;
			} else {
				System.out.println("Great! Let's Play.");
				proceed = false;
			}
		} while (proceed);

		for (String player : playerNames) {
			GamePlayer gameplayer = new GamePlayer();
			gameplayer.setPlayerName(player);
			playersList.add(gameplayer);
		}

		do {
			populateFlag = false;
			System.out.println("Enter Command to Populate Country to Players");
			String input = br.readLine().trim();
			Pattern commandName = Pattern.compile("populatecountries");
			Matcher commandMatch = commandName.matcher(input);
			if (!commandMatch.matches() || input.isEmpty()) {
				System.out.println("\nIncorrect Command");
				populateFlag = true;
			}
			populateCountries(mapGraph);

		} while (populateFlag);

	}

	public void populateCountries(GameMap gameMap) {
		int countryIndex;
		ArrayList<Country> countrySet = new ArrayList<>(gameMap.getCountrySet().values());
		while (countrySet.size() > 0) {
			for (int i = 0; i < this.playersList.size(); ++i) {
				if (countrySet.size() > 1) {
					countryIndex = new Random().nextInt(countrySet.size());
					this.playersList.get(i).addCountry(countrySet.get(countryIndex));
					countrySet.remove(countryIndex);
				} else if (countrySet.size() == 1) {
					this.playersList.get(i).addCountry(countrySet.get(0));
					countrySet.remove(0);
					break;
				} else {
					break;
				}
			}
		}
	}

}
