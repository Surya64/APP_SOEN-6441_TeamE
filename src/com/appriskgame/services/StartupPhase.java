package com.appriskgame.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public class StartupPhase {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	ArrayList<GamePlayer> playersList = new ArrayList<GamePlayer>();
	ArrayList<String> playerNames;
	GamePlayer gameplayer = new GamePlayer();
	static int TWOPLAYERARMYCOUNT = 40;
	static int THREEPLAYERARMYCOUNT = 35;
	static int FOURPLAYERARMYCOUNT = 30;
	static int FIVEPLAYERARMYCOUNT = 25;
	static int SIXPLAYERARMYCOUNT = 20;

	public void gamePlay(GameMap gameMap) throws Exception {
		boolean proceed = false, populateFlag = false;

		do {
			boolean flag;
			playerNames = new ArrayList<String>();
			do {
				flag = false;
				System.out.println("Enter Command to add or remove player");
				String input = br.readLine().trim();
				Pattern commandPattern = Pattern.compile("[a-zA-z]+ -[a-zA-z\\s-]*");
				Pattern commandName = Pattern.compile("gameplayer");
				Matcher commandMatch = commandPattern.matcher(input);
				String[] command = input.split("-");
				Matcher commandNameMatch = commandName.matcher(command[0].trim());
				if (!commandMatch.matches() || input.isEmpty() || !commandNameMatch.matches()) {
					System.out.println("\nIncorrect Command");
					flag = true;
				} else {
					if (input.contains("-add")) {
						String[] addData = input.split("-add ");
						for (int i = 1; i < addData.length; i++) {
							String name = addData[i].trim();
							Pattern namePattern = Pattern.compile("[a-zA-z]+");
							Matcher match = namePattern.matcher(name);
							if (!match.matches() || name.isEmpty()) {
								System.out.println("\nPlease enter the correct player name");
								flag = true;
							}
							playerNames.add(name);
						}
					} else if (input.contains("-remove")) {
						String[] removeData = input.split("-remove ");
						for (int i = 1; i < removeData.length; i++) {
							String name = removeData[i].trim();
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
				System.out.println("Do you want to add/remove players? Yes/No");
				String choice = br.readLine().trim();
				while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No") || choice == null)) {
					System.err.println("\nPlease enter the choice as either Yes or No:");
					choice = br.readLine().trim();
				}

				if (choice.equalsIgnoreCase("Yes")) {
					flag = true;
				} else {
					flag = false;
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
			GamePlayer gamePlayers = new GamePlayer();
			gamePlayers.setPlayerName(player);
			playersList.add(gamePlayers);
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
			populateCountries(gameMap);

		} while (populateFlag);
		defaultArmiesToPlayer();
		initialArmyAllocation(gameMap);

		RoundRobinAllocator roundRobin = new RoundRobinAllocator(playersList);
		System.out.println("Do you want to place army Individually? Yes/No");
		String choice = br.readLine().trim();
		while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No") || choice == null)) {
			System.err.println("\nPlease enter the choice as either Yes or No:");
			choice = br.readLine().trim();
		}

		if (choice.equalsIgnoreCase("Yes")) {
			while (playersList.get(0).getNoOfArmies() > 0) {
				for (int round = 1; round <= playersList.size(); round++) {
					gameplayer = roundRobin.nextTurn();
					boolean placeArmyFlag = true;
					System.out.println("Name: " + gameplayer.getPlayerName());
					System.out.println("Countries: " + gameplayer.getPlayerCountries());
					System.out.println("No of Armies remaining: " + gameplayer.getNoOfArmies());
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
							gameplayer.getPlayerCountries().forEach(con -> {
								if (con.getCountryName().equals(data[1])) {
									Country selectedCountry = con;
									if (gameplayer.getNoOfArmies() <= 0) {
										selectedCountry.setNoOfArmies(selectedCountry.getNoOfArmies() + 1);
										gameplayer.setNoOfArmies(gameplayer.getNoOfArmies() - 1);
									} else {
										System.out.println("All armies are placed.\n");
									}
								}
							});
						}

					} while (placeArmyFlag);

				}
			}
		} else {
			boolean placeAllArmyFlag = true;
			do {
				placeAllArmyFlag = false;
				System.out.println("Enter Command to place all remaining armies");
				String input = br.readLine().trim();
				Pattern commandName = Pattern.compile("placeall");
				Matcher commandMatch = commandName.matcher(input);
				if (!commandMatch.matches() || input.isEmpty()) {
					System.out.println("\nIncorrect Command");
					placeAllArmyFlag = true;
				}
				if (!placeAllArmyFlag) {
					for (int round = 1; round <= playersList.size(); round++) {
						gameplayer = roundRobin.nextTurn();
						while (gameplayer.getNoOfArmies() > 0) {
							int index = new Random().nextInt(gameplayer.getPlayerCountries().size());
							Country selectedCountry = gameplayer.getPlayerCountries().get(index);
							if (gameplayer.getNoOfArmies() > 0) {
								selectedCountry.setNoOfArmies(selectedCountry.getNoOfArmies() + 1);
								gameplayer.setNoOfArmies(gameplayer.getNoOfArmies() - 1);
							} else {
								System.out.println("All armies are placed.\n");
							}
						}
						System.out.println("Name: " + gameplayer.getPlayerName());
						System.out.println("Countries: " + gameplayer.getPlayerCountries());
						System.out.println("No of Armies remaining: " + gameplayer.getNoOfArmies());
					}
				}

			} while (placeAllArmyFlag);

		}
		System.out.println("\n\nReinforcement Phase Begins\n\n");
		System.out.println("Do you want to perfom Reinforcement Phase");
		String input = br.readLine().trim();
		while (!(input.equalsIgnoreCase("Yes") || input.equalsIgnoreCase("No") || input == null)) {
			System.err.println("\nPlease enter the choice as either Yes or No:");
			input = br.readLine().trim();
		}
		if (input.equalsIgnoreCase("Yes")) {
			ReinforcementPhase reinforce = new ReinforcementPhase();
			for (int round = 1; round <= playersList.size(); round++) {
				gameplayer = roundRobin.nextTurn();
				Continent playerContinent = gameplayer.getPlayerCountries().get(0).getPartOfContinent();
			    int reInforceAmries = ReinforcementPhase.assignReinforcedArmies(gameplayer, playerContinent);
			    gameplayer.setNoOfArmies(reInforceAmries);
			    while(gameplayer.getNoOfArmies()>0) {
				reinforce.startReinforcement(gameplayer, gameMap);
			    }
			    System.out.println("Attack Begin");
			    System.out.println("Attack Ends");
			    System.out.println("Fortification Phase Begins");
			    
			}
		} else {
			System.out.println("Thank You!!");
		}

	}

	public void populateCountries(GameMap gameMap) {
		int countryIndex;
		ArrayList<Country> countrySet = new ArrayList<>(gameMap.getCountrySet().values());
		while (countrySet.size() > 0) {
			for (int i = 0; i < playersList.size(); ++i) {
				if (countrySet.size() > 1) {
					countryIndex = new Random().nextInt(countrySet.size());
					playersList.get(i).addCountry(countrySet.get(countryIndex));
					countrySet.remove(countryIndex);

				} else if (countrySet.size() == 1) {
					playersList.get(i).addCountry(countrySet.get(0));
					countrySet.remove(0);
					break;
				} else {
					break;
				}
			}
		}
	}

	public void defaultArmiesToPlayer() {
		playersList.forEach(player -> {
			switch (playersList.size()) {
			case 2:
				player.setNoOfArmies(TWOPLAYERARMYCOUNT);
				break;
			case 3:
				player.setNoOfArmies(THREEPLAYERARMYCOUNT);
				break;
			case 4:
				player.setNoOfArmies(FOURPLAYERARMYCOUNT);
				break;
			case 5:
				player.setNoOfArmies(FIVEPLAYERARMYCOUNT);
				break;
			case 6:
				player.setNoOfArmies(SIXPLAYERARMYCOUNT);
				break;
			}
		});
	}

	public void initialArmyAllocation(GameMap gameMap) {

		gameMap.getCountrySet().values().forEach(country -> {
			country.setNoOfArmies(1);
		});

		gameMap.getCountrySet().values().forEach(country -> {
			playersList.forEach(player -> {
				if (player.getPlayerCountries().contains(country)) {
					country.setPlayer(player.getPlayerName());
				}
			});
		});

		playersList.forEach(player -> {
			player.setNoOfArmies(player.getNoOfArmies() - player.getPlayerCountries().size());
		});
	}

}
