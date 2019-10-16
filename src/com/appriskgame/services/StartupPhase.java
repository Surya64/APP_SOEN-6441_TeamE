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

/**
 * This class contains methods which will take the input to add or remove
 * players , Populate countries to players randomly , to allocate armies to the
 * players initially and then place the remaining armies in round robin fashion
 * and includes to initialize the reinforcement phase
 * 
 * @author Sahana
 * @author Surya
 */
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

	/**
	 * This method is used to set the player's list.
	 * 
	 * @param playersList It is the player's List need to be set
	 */
	public void setPlayerList(ArrayList<GamePlayer> playersList) {
		this.playersList = playersList;
	}

	/**
	 * This method is used to get the player's list.
	 * 
	 * @return ArrayList of Players
	 */
	public ArrayList<GamePlayer> getPlayerList() {
		return playersList;
	}

	/**
	 * This method starts the game by obtaining the number of players and taking the
	 * input from the players and initialize them.
	 * 
	 * @param gameMap Object of GameMap which consists of Map details
	 * @throws Exception IO Exception
	 */
	public void gamePlay(GameMap gameMap) throws Exception {
		boolean proceed = false, populateFlag = false, mapFlag = true;
		playerNames = new ArrayList<String>();
		do {
			boolean flag;
			do {
				flag = false;
				System.out.println("Enter Command to add or remove player");
				String input = br.readLine().trim();
				Pattern multiPattern = Pattern.compile("[a-zA-z]+ [-[a-z]+ [a-zA-Z]+ ]*");
				Matcher multiCommandPattern = multiPattern.matcher(input);
				if (!multiCommandPattern.matches() || input.isEmpty()) {
					System.out.println("\nIncorrect Command");
					flag = true;
				}
				if (!flag) {
					ArrayList<String> multiCommand = multipleCommands(input);
					for (int p = 0; p < multiCommand.size(); p++) {
						String data = multiCommand.get(p);
						Pattern commandPattern = Pattern.compile("[a-zA-z]+ -[a-zA-z\\s-]*");
						Pattern commandName = Pattern.compile("gameplayer");
						Matcher commandMatch = commandPattern.matcher(data);
						String[] command = data.split("-");
						Matcher commandNameMatch = commandName.matcher(command[0].trim());
						if (!commandMatch.matches() || input.isEmpty() || !commandNameMatch.matches()) {
							System.out.println("\nIncorrect Command");
							flag = true;
						}
						if (!flag) {
							if (data.contains("-add")) {
								String[] addData = data.split("-add ");
								for (int i = 1; i < addData.length; i++) {
									String name = addData[i].trim();
									Pattern namePattern = Pattern.compile("[a-zA-z0-9]+");
									Matcher match = namePattern.matcher(name);
									if (!match.matches() || name.isEmpty()) {
										System.out.println("\nPlease enter the correct player name");
										flag = true;
									}
									playerNames.add(name);
								}
							} else if (data.contains("-remove")) {
								String[] removeData = data.split("-remove ");
								for (int i = 1; i < removeData.length; i++) {
									String name = removeData[i].trim();
									Pattern namePattern = Pattern.compile("[a-zA-z0-9]+");
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
				}
			} while (flag);
			if (playerNames.size() > 5 || playerNames.size() < 2) {
				System.out.println("Sorry! The numbers of players can be between 2 and 6.\n Current size is "
						+ playerNames.size() + "\nPlayers are : " + playerNames);
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
			if (!populateFlag) {
				populateCountries(gameMap);
			}

		} while (populateFlag);
		defaultArmiesToPlayer();
		initialArmyAllocation(gameMap);

		do {
			System.out.println("Enter the Command to display Map");
			String mapCommand = br.readLine().trim();
			if (mapCommand.equalsIgnoreCase("showmap")) {
				showMap(gameMap);
				mapFlag = false;
			} else {
				System.out.println("Incorrect Command");
				mapFlag = true;
			}
		} while (mapFlag);

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
					System.out.println("No of Armies remaining: " + gameplayer.getNoOfArmies());
					do {
						placeArmyFlag = false;
						boolean middlePlace = false;
						System.out.println("Enter Command to place Army to Country");
						String input = br.readLine().trim();
						if (input.equalsIgnoreCase("placeall")) {
							for (int i = 1; i <= playersList.size(); i++) {
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
								System.out.println("Player Name: " + gameplayer.getPlayerName());
								System.out.println("Countries: " + gameplayer.getPlayerCountries());
								System.out.println("No of Armies remaining: " + gameplayer.getNoOfArmies());
							}
							middlePlace = true;
							placeArmyFlag = false;
							round = playerNames.size();
						}
						if (!middlePlace) {
							String[] data = input.split(" ");
							Pattern commandName = Pattern.compile("placearmy");
							Matcher commandMatch = commandName.matcher(data[0]);
							if (!commandMatch.matches() || input.isEmpty()) {
								System.out.println("\nIncorrect Command");
								placeArmyFlag = true;
							}
							if (!placeArmyFlag) {
								boolean ownCountryFlag = false;
								ArrayList<Country> playerCountries = gameplayer.getPlayerCountries();
								for (int i = 0; i < playerCountries.size(); i++) {
									if (playerCountries.get(i).getCountryName().equalsIgnoreCase(data[1])) {
										if (gameplayer.getNoOfArmies() > 0) {
											playerCountries.get(i)
													.setNoOfArmies(playerCountries.get(i).getNoOfArmies() + 1);
											gameplayer.setNoOfArmies(gameplayer.getNoOfArmies() - 1);
											System.out.println(
													"One Army is placed in " + playerCountries.get(i).getCountryName());
											ownCountryFlag = true;
										} else {
											System.out.println("All armies are placed.\n");
											ownCountryFlag = true;
											placeArmyFlag = false;
										}
									}
								}
								if (!ownCountryFlag) {
									System.out.println("Please enter the Country that you Own");
									placeArmyFlag = true;
								}
							}
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
						System.out.println("Player Name: " + gameplayer.getPlayerName());
						System.out.println("Countries: " + gameplayer.getPlayerCountries());
						System.out.println("No of Armies remaining: " + gameplayer.getNoOfArmies());
					}
				}

			} while (placeAllArmyFlag);

		}
		do {
			System.out.println("Enter the Command to display Map");
			String mapCommand = br.readLine().trim();
			if (mapCommand.equalsIgnoreCase("showmap")) {
				showMap(gameMap);
				mapFlag = false;
			} else {
				System.out.println("Incorrect Command");
				mapFlag = true;
			}
		} while (mapFlag);
		ReinforcementPhase reinforce = new ReinforcementPhase();
		FortificationPhase fortify = new FortificationPhase();
		boolean gameContinue;
		do {
			gameContinue = false;
			for (int round = 1; round <= playersList.size(); round++) {
				gameplayer = roundRobin.nextTurn();
				System.out.println("** Reinforcement Phase Begins for Player: " + gameplayer.getPlayerName() + " **");
				System.out.println(gameplayer.getPlayerCountries());
				Continent playerContinent = gameplayer.getPlayerCountries().get(0).getPartOfContinent();
				int reInforceAmries = ReinforcementPhase.assignReinforcedArmies(gameplayer, playerContinent);
				gameplayer.setNoOfArmies(reInforceAmries);
				while (gameplayer.getNoOfArmies() > 0) {
					reinforce.startReinforcement(gameplayer, gameMap);
				}
				System.out.println("** Reinforcement Phase Ends for Player: " + gameplayer.getPlayerName() + " **");
				System.out.println("Attack Begin");
				System.out.println("Attack Ends");
				System.out.println("** Fortification Phase Begins for Player: " + gameplayer.getPlayerName() + " **");
				System.out.println("Enter the Command to display Map");
				String mapCommand = br.readLine().trim();
				if (mapCommand.equalsIgnoreCase("showmap")) {
					showMap(gameMap);
				}
				fortify.startGameFortification(gameplayer, gameMap);
				System.out.println("** Fortification Phase Ends for Player: " + gameplayer.getPlayerName() + " **");

			}
			System.out.println("Do you want to continue playing? Yes/No");
			String continuePlaying = br.readLine().trim();
			while (!(continuePlaying.equalsIgnoreCase("Yes") || continuePlaying.equalsIgnoreCase("No")
					|| continuePlaying == null)) {
				System.err.println("\nPlease enter the choice as either Yes or No:");
				choice = br.readLine().trim();
			}
			if (choice.equalsIgnoreCase("Yes")) {
				gameContinue = true;
			} else {
				gameContinue = false;
				System.out.println("Thank You!!");
				System.exit(0);
			}
		} while (gameContinue);

	}

	/**
	 * This method is used show the details of the countries and continents, armies
	 * on each country, ownership of each country.
	 * 
	 * @param gameMap Object of GameMap which consists of Map details
	 */
	public void showMap(GameMap gameMap) {
		ArrayList<Country> print = gameMap.getCountries();
		for (Country country : print) {
			System.out.println(country);
		}
	}

	/**
	 * This method is used to assign countries to the players Random allocation of
	 * countries to players will take place in this method.
	 * 
	 * @param gameMap Object of GameMap which consists of Map details
	 */
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

	/**
	 * This method is used to allocate Armies to players which depends on the
	 * players count in the game.
	 * 
	 */
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

	/**
	 * This method is used to assign armies to the countries so that each country
	 * will get at least one army according to the game rule.
	 * 
	 * @param gameMap Object of mapGraph which consists of map details
	 */
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

	/**
	 * Method to form the single command
	 * 
	 * @param cmdDetails command string
	 * @return single command as string
	 */
	public String singleCommandOperation(String cmdDetails[]) {
		String command = "";
		for (int i = 0; i < cmdDetails.length; i++) {
			command = command + cmdDetails[i] + " ";
		}
		return command.trim();
	}

	/**
	 * This method is used to split the full command into single command of list
	 * 
	 * @param fullCommand input command with multiple add and remove
	 * @return single command in arraylist
	 */
	public ArrayList<String> multipleCommands(String fullCommand) {
		String[] commandArrays = fullCommand.split(" ");
		boolean suspend = false;
		ArrayList<String> splitCommands = new ArrayList<String>();

		for (int i = 1; i < commandArrays.length && suspend == false; i = i + 1) {
			String[] cmdDetails = new String[3];
			String decider = commandArrays[i];

			switch (decider) {
			case "-add":
				cmdDetails[0] = "gameplayer";
				cmdDetails[1] = "-add";
				i = i + 1;
				cmdDetails[2] = commandArrays[i];
				String addCoammand = singleCommandOperation(cmdDetails);
				splitCommands.add(addCoammand);
				suspend = false;
				break;
			case "-remove":
				cmdDetails = new String[3];
				cmdDetails[0] = "gameplayer";
				cmdDetails[1] = "-remove";
				i = i + 1;
				cmdDetails[2] = commandArrays[i];
				String removeCommand = singleCommandOperation(cmdDetails);
				splitCommands.add(removeCommand);
				suspend = false;
				break;
			}
		}
		return splitCommands;
	}

}
