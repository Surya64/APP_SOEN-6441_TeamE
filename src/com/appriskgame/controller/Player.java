package com.appriskgame.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class Player {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	ArrayList<GamePlayer> playersList = new ArrayList<GamePlayer>();
	ArrayList<String> playerNames;
	RoundRobinAllocator roundRobin;
	MapOperations mapOperations = new MapOperations();
	GamePlayer gameplayer = new GamePlayer();
	static int TWOPLAYERARMYCOUNT = 40;
	static int THREEPLAYERARMYCOUNT = 35;
	static int FOURPLAYERARMYCOUNT = 30;
	static int FIVEPLAYERARMYCOUNT = 25;
	static int SIXPLAYERARMYCOUNT = 20;
	public static int MINIMUM_REINFORCEMENT_ARMY = 3;
	public static int MINIMUM_NUM_OF_PLAYERS_COUNTRY = 9;

	public static ArrayList<Country> listOfContries = new ArrayList<Country>();
	public static ArrayList<Country> listOfCountriesOfPlayersContinent = new ArrayList<Country>();
	public static ArrayList<Continent> listOfPlayerContinents = new ArrayList<Continent>();
	public static String playersChoice;
	public static List<String> playersChoiceList = new ArrayList<String>();

	public boolean doFortification = false;

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

		roundRobin = new RoundRobinAllocator(playersList);
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
							placeallAmry();
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
					placeallAmry();
				}
			} while (placeAllArmyFlag);

		}
		boolean gameContinue;
		AttackPhase attack = new AttackPhase();
		do {
			gameContinue = false;
			for (int round = 1; round <= playersList.size(); round++) {
				gameplayer = roundRobin.nextTurn();
				System.out.println("** Reinforcement Phase Begins for Player: " + gameplayer.getPlayerName() + " **");
				System.out.println(gameplayer.getPlayerCountries());
				Continent playerContinent = gameplayer.getPlayerCountries().get(0).getPartOfContinent();
				int reInforceAmries = assignReinforcedArmies(gameplayer, playerContinent);
				gameplayer.setNoOfArmies(reInforceAmries);
				while (gameplayer.getNoOfArmies() > 0) {
					startReinforcement(gameplayer, gameMap);
				}
				System.out.println("** Reinforcement Phase Ends for Player: " + gameplayer.getPlayerName() + " **");
				System.out.println("Attack Begin");
				attack.attackPhaseControl(playersList,gameplayer, gameMap);
				System.out.println("Attack Ends");
				System.out.println("** Fortification Phase Begins for Player: " + gameplayer.getPlayerName() + " **");
				startGameFortification(gameplayer, gameMap);
				System.out.println("** Fortification Phase Ends for Player: " + gameplayer.getPlayerName() + " **");
			}
			System.out.println("Do you want to continue playing? Yes/No");
			String continuePlaying = br.readLine().trim();
			while (!(continuePlaying.equalsIgnoreCase("Yes") || continuePlaying.equalsIgnoreCase("No")
					|| continuePlaying == null)) {
				System.err.println("\nPlease enter the choice as either Yes or No:");
				choice = br.readLine().trim();
			}
			if (continuePlaying.equalsIgnoreCase("Yes")) {
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

	//startup phase methods
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
	 * This method is used to split the full command into single command of list
	 * 
	 * @param fullCommand input command with multiple add and remove
	 * @return single command in arrayList
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
				String addCoammand = mapOperations.singleCommandOperation(cmdDetails);
				splitCommands.add(addCoammand);
				suspend = false;
				break;
			case "-remove":
				cmdDetails = new String[3];
				cmdDetails[0] = "gameplayer";
				cmdDetails[1] = "-remove";
				i = i + 1;
				cmdDetails[2] = commandArrays[i];
				String removeCommand = mapOperations.singleCommandOperation(cmdDetails);
				splitCommands.add(removeCommand);
				suspend = false;
				break;
			}
		}
		return splitCommands;
	}

	/**
	 * This method is used place all the remaining armies randomly to each player in
	 * a round robin fashion
	 */
	public void placeallAmry() {
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

	// reinforcement Methods

	/**
	 * This method asks the player to be continued with Reinforcement phase. If the
	 * player choose to continue this will call a method assigningReinforcedArmies
	 *
	 * @param player     - player details for reinforcement
	 * @param mapDetails -Object of map
	 * @throws Exception -IOException
	 */
	public void startReinforcement(GamePlayer player, GameMap mapDetails) throws Exception {
		Continent playerContinent = player.getPlayerCountries().get(0).getPartOfContinent();
		int sizeOfPlayerCountries = player.getPlayerCountries().size();
		for (int i = 0; i < sizeOfPlayerCountries; i++) {
			playerContinent = player.getPlayerCountries().get(i).getPartOfContinent();
			if (!listOfPlayerContinents.contains(playerContinent)) {
				listOfPlayerContinents.add(playerContinent);
			}
		}
		for (int i = 0; i < listOfPlayerContinents.size(); i++) {
			listOfCountriesOfPlayersContinent.addAll(listOfPlayerContinents.get(i).getListOfCountries());
		}
		Country countryNameObject = new Country();
		ArrayList<Country> numOfcontries = mapDetails.getCountries();
		for (int i = 0; i < numOfcontries.size(); i++) {
			Country country = numOfcontries.get(i);
			if (country.getPartOfContinent().getContinentName().equalsIgnoreCase(playerContinent.getContinentName())) {
				listOfContries.add(country);
			}
		}
		int reinforcementArmies = assignReinforcedArmies(player, playerContinent);
		player.setNoOfArmies((reinforcementArmies));

		while (player.getNoOfArmies() > 0) {
			System.out.println(" Player Name :" + player.getPlayerName());
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
					userAssignedArmiesToCountries(country, numOfarmies, player);
				}
			}
		}
	}

	/**
	 *
	 * This method check the army count entered by the user and if it is less than
	 * the available, it assigned to the mentioned country
	 *
	 * @param country     - the country given to players
	 * @param armiesCount - the count of the armies player has
	 * @param player      - Current player object
	 */
	public void userAssignedArmiesToCountries(Country country, int armiesCount, GamePlayer player) {
		if (player.getPlayerCountries().contains(country)) {
			if ((player.getNoOfArmies()) > 0 && player.getNoOfArmies() >= armiesCount) {
				country.setNoOfArmies(country.getNoOfArmies() + armiesCount);
				player.setNoOfArmies(player.getNoOfArmies() - armiesCount);
			} else {
				System.out.println("Insufficient number of armies.\n");
			}
		} else {
			System.out.println("This country is not owned by you!\n");
		}
	}

	/**
	 * Based on Reinforcement conditions the player will be allocated with some
	 * armies to assign to countries
	 *
	 * @param player    - The player to whom armies will be allocated to
	 * @param continent - Continent to which the player belongs to
	 * @return armies to be assigned to any country of players choice
	 */
	public int assignReinforcedArmies(GamePlayer player, Continent continent) {
		int contriesPlyerOwns = player.getPlayerCountries().size();
		int reinformentArmiesAssigned;
		if (contriesPlyerOwns >= MINIMUM_NUM_OF_PLAYERS_COUNTRY) {
			reinformentArmiesAssigned = (int) Math.floor(contriesPlyerOwns / 3);
		} else {
			reinformentArmiesAssigned = MINIMUM_REINFORCEMENT_ARMY;
		}
		for (int i = 0; i < listOfPlayerContinents.size(); i++) {
			if (doesPlayerOwnAContinent(player, listOfPlayerContinents.get(i).getListOfCountries()))
				reinformentArmiesAssigned = reinformentArmiesAssigned
						+ listOfPlayerContinents.get(i).getContinentControlValue();
		}
		return reinformentArmiesAssigned;
	}

	/**
	 * This method is to check whether player owns whole continent
	 *
	 * @param player        - The player whose reinforcement turn is on
	 * @param countriesList - The count of countries a continent has
	 * @return True if a player own a continent
	 */
	private static boolean doesPlayerOwnAContinent(GamePlayer player, ArrayList<Country> countriesList) {
		boolean flag = true;
		for (int i = 0; i < countriesList.size(); i++) {
			if (!player.getPlayerCountries().contains(countriesList.get(i)))
				flag = false;
		}
		return flag;
	}

	// fortification methods

	/**
	 * This method is called from the Startup phase when the user opts to start the
	 * fortification. It internally calls the moveArmies method once all the
	 * validation with respect to fortification are performed.
	 *
	 * @param player  - The player who is doing fortification.
	 * @param gameMap - GameMap object
	 * @throws IOException - throws Input-Output exception
	 */
	public void startGameFortification(GamePlayer player, GameMap gameMap) throws IOException {
		List<String> playersCommandList = new ArrayList<String>();
		String strUser;
		String strfromCountry = "";
		String strtoCountry = "";
		String countryNumToPlace = "";
		String cmd;
		int countOfArmies = 0;
		if (player.getPlayerCountries().size() >= 2) {
			doFortification = true;
			boolean doFortificationNone = false;
			while (doFortification && !doFortificationNone) {
				doFortification = true;
				doFortificationNone = false;
				Country givingCountry = null;
				Country receivingCountry = null;
				do {
					doFortification = false;
					doFortificationNone = false;
					System.out.println("\nPlayer has the following list of countries with armies: \n");
					for (Country country : player.getPlayerCountries()) {
						System.out.println("* " + country.getCountryName() + ":" + country.getNoOfArmies() + "\n");
					}
					System.out.println("Enter the Command for fortification");
					strUser = br.readLine().trim();
					playersCommandList = Arrays.asList(strUser.split(" "));
					if (playersCommandList.size() == 2) {
						playersCommandList = Arrays.asList(strUser.split(" "));
						String none = playersCommandList.get(1);
						cmd = playersCommandList.get(0);
						if (none.equalsIgnoreCase("none") && cmd.equalsIgnoreCase("fortify")) {
							System.out.println("No Move in Forification Phase");
							doFortificationNone = true;
							doFortification = true;
						} else {
							System.out.println("Please enter the right format like : fortify none");
						}
					}

					if (!doFortification) {
						if (!(playersCommandList.size() == 4)) {
							System.out
									.println("Please enter the right format like : fortify fromcountry tocountry num");
							strUser = br.readLine().trim();
							playersCommandList = Arrays.asList(strUser.split(" "));
						}
						strfromCountry = playersCommandList.get(1);
						strtoCountry = playersCommandList.get(2);
						countryNumToPlace = playersCommandList.get(3);
						cmd = playersCommandList.get(0);
						Pattern cmdPattern = Pattern.compile("fortify");
						Matcher cmdMatch = cmdPattern.matcher(cmd);
						Pattern namePattern = Pattern.compile("[a-zA-Z-_]+");
						Matcher matchFromCountry = namePattern.matcher(strfromCountry);
						Matcher matchToCountry = namePattern.matcher(strtoCountry);
						Pattern numberPattern = Pattern.compile("[0-9]+");
						Matcher match1 = numberPattern.matcher(countryNumToPlace);
						while (!matchFromCountry.matches() || strfromCountry.isEmpty() || !matchToCountry.matches()
								|| strtoCountry.isEmpty() || !match1.matches() || countryNumToPlace.isEmpty()
								|| !cmdMatch.matches()) {
							if (!matchFromCountry.matches() || strfromCountry.isEmpty()) {
								System.out.println("\nInCorrect fromcountry name, please enter the command again:");
								strUser = br.readLine().trim();
								playersCommandList = Arrays.asList(strUser.split(" "));
								strfromCountry = playersCommandList.get(1);
								strtoCountry = playersCommandList.get(2);
								countryNumToPlace = playersCommandList.get(3);
							}
							if (!matchToCountry.matches() || strtoCountry.isEmpty()) {
								System.out.println("\nInCorrect tocountry name, please enter the command again:");
								strUser = br.readLine().trim();
								playersCommandList = Arrays.asList(strUser.split(" "));
								strfromCountry = playersCommandList.get(1);
								strtoCountry = playersCommandList.get(2);
								countryNumToPlace = playersCommandList.get(3);
							}
							if (!match1.matches() || countryNumToPlace.isEmpty()) {
								System.out.println("\nInCorrect Army Count, please enter the command again:");
								strUser = br.readLine().trim();
								playersCommandList = Arrays.asList(strUser.split(" "));
								strfromCountry = playersCommandList.get(1);
								strtoCountry = playersCommandList.get(2);
								countryNumToPlace = playersCommandList.get(3);
								match1 = numberPattern.matcher(countryNumToPlace);
							}
							if (!cmdMatch.matches()) {
								System.out.println("\nInCorrect Command, please enter the command again:");
								strUser = br.readLine().trim();
								playersCommandList = Arrays.asList(strUser.split(" "));
								cmd = playersCommandList.get(0);
								strfromCountry = playersCommandList.get(1);
								strtoCountry = playersCommandList.get(2);
								countryNumToPlace = playersCommandList.get(3);
								cmdMatch = cmdPattern.matcher(cmd);
							}
						}
						for (Country country : player.getPlayerCountries()) {
							if (country.getCountryName().equalsIgnoreCase(strfromCountry)) {
								givingCountry = country;
							}
						}
						for (Country country : player.getPlayerCountries()) {
							if (country.getCountryName().equalsIgnoreCase(strtoCountry)) {
								receivingCountry = country;
							}
						}
						if (player.getPlayerCountries().contains(givingCountry)
								&& player.getPlayerCountries().contains(receivingCountry)) {
							doFortification = false;
						} else {
							System.out.println(
									"Entered countries doesn't exist in player's owned country list, please enter country names again\n");
							doFortification = true;
						}
					}
				} while (doFortification && !doFortificationNone);
				if (!doFortification) {
					countOfArmies = Integer.parseInt(countryNumToPlace);
					if (isArmyCountSufficient(countOfArmies, givingCountry)) {
						doFortification = true;
					}
				}
				if (!doFortification) {
					boolean fortify = false;
					for (Country country : player.getPlayerCountries()) {
						for (String temp : country.getNeighbourCountries()) {
							if (temp.equalsIgnoreCase(strfromCountry) || temp.equalsIgnoreCase(strtoCountry)) {
								fortify = true;
							}
						}
					}
					if (fortify) {
						moveArmies(givingCountry, receivingCountry, countOfArmies);
					} else {
						doFortification = true;
						System.out
								.println("None of the players' countries are adjacent\n Fortification phase ends..!!");
					}
				}

			}

		} else {
			System.out.println("Sorry, Fortification is not possible if the country owned is less than 2");
		}
	}

	/**
	 * This method checks if the provided army count is sufficient for
	 * fortification.
	 * 
	 * @param armyCount     Number of armies to move
	 * @param givingCountry Country name where armies are moved from
	 * @return true if the army count is insufficient or else false
	 */
	public boolean isArmyCountSufficient(int armyCount, Country givingCountry) {
		if (armyCount >= givingCountry.getNoOfArmies()) {
			System.out.println(
					"Insufficient armies available, fortification is not possible with asked number of armies.");
			return true;
		}
		if (givingCountry.getNoOfArmies() == 1) {
			System.out.println("Insufficient armies available, " + givingCountry.getCountryName()
					+ " should have more than 1 army to Move");
			return true;
		}
		if (armyCount < 0) {
			System.out.println("Army count should be a positive number");
			return true;
		}
		return false;

	}

	/**
	 * This method takes the values for each player from the startFortification
	 * method and does the manipulation of armies and assign the armies
	 *
	 * @param fromCountry - The country from where player want to move army
	 * @param toCountry   - The country to where player want to move army
	 * @param armiesCount - Count of armies player wish to move
	 */
	public void moveArmies(Country fromCountry, Country toCountry, int armiesCount) {

		boolean neighbourCountries = false;
		for (String country : fromCountry.getNeighbourCountries()) {
			if (country.equalsIgnoreCase(toCountry.getCountryName())) {
				int fromCountryArmy = fromCountry.getNoOfArmies();
				int toCountryArmy = toCountry.getNoOfArmies();
				fromCountry.setNoOfArmies(fromCountryArmy - armiesCount);
				toCountry.setNoOfArmies(toCountryArmy + armiesCount);
				neighbourCountries = true;
				doFortification = false;
				System.out.println("\nArmies successfully moved!");
				System.out.println("\nFortification phase ends!");
				break;
			}
		}
		if (!neighbourCountries) {
			System.out.println("Countries are not adjacent!");
			doFortification = true;
		}
	}

}
