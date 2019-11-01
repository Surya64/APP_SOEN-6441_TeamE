
package com.appriskgame.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public class AttackPhase {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public void attackPhaseControl(ArrayList<GamePlayer> playersList, GamePlayer player, GameMap mapDetails)
			throws IOException {
		boolean gameContinue;

		do {
			boolean errorOccured = false;
			String errorDetails = "";
			gameContinue = false;
			showMap(mapDetails);
			System.out.println("Enter the Attacker command?" + player.getPlayerName());
			String userCommand = br.readLine().trim();
			if (checkUserValidation(userCommand)) {
				String[] attackDetails = userCommand.split(" ");
				String attackCountry = attackDetails[1];
				if (attackCountry.equalsIgnoreCase("-noattack")) {
					break;
				}
				String defenderCountry = attackDetails[2];
				String decision = attackDetails[3];
				boolean attackerCountryPresent = isCountryAttackPresent(player, attackCountry, mapDetails);
				boolean defenderCountryPresent = isCountryPresent(defenderCountry, mapDetails);
				Country attackCountryObject = null;
				Country defenderCountryObject = null;
				if (!attackerCountryPresent) {
					errorDetails = attackCountry + " is not owned by the current player" + "\n";
					errorOccured = true;
				}
				if (!defenderCountryPresent) {
					errorDetails = errorDetails + defenderCountry + "This is not in map" + "\n";
					errorOccured = true;
				}
				boolean isAttackAndDefenderAdajacent = false;
				if (attackerCountryPresent && defenderCountryPresent) {
					attackCountryObject = getCountryObject(attackCountry, mapDetails);
					defenderCountryObject = getCountryObject(defenderCountry, mapDetails);

					isAttackAndDefenderAdajacent = isCountryAdjacent(attackCountryObject, defenderCountry, mapDetails);
				}
				if (defenderCountryPresent) {
					if (defenderCountryObject != null) {
						if (defenderCountryObject.getPlayer().equalsIgnoreCase(player.getPlayerName())) {
							errorDetails = defenderCountry + " is  owned by the current player" + "\n";
							errorOccured = true;
						}
					}
				}
				if (!isAttackAndDefenderAdajacent) {
					if (attackerCountryPresent) {
						errorDetails = errorDetails + attackCountry + " is not adjacent to the " + defenderCountry;
						errorOccured = true;
					}
				}
				if (decision.equalsIgnoreCase("-allout") && errorOccured == false) {
					while (attackCountryObject.getNoOfArmies() > 1 && defenderCountryObject.getNoOfArmies() != 0) {
						int attackerDices = maxAllowableAttackerDice(attackCountryObject.getNoOfArmies());
						int defenderDices = maxAllowableDefenderDice(defenderCountryObject.getNoOfArmies());
						if (attackerDices > 0 && defenderDices > 0) {
							attackingStarted(attackerDices, defenderDices, attackCountryObject, defenderCountryObject);
							if (isAttackerWon(defenderCountryObject)) {
								moveArmyToConquredCountry(playersList, player, attackCountryObject,
										defenderCountryObject);
								break;
							}
						}
					}
				} else if (errorOccured == false) {
					int attackerDices = Integer.parseInt(attackDetails[3]);
					int attackerArmies = attackCountryObject.getNoOfArmies();
					if (isAttackerDicePossible(attackerArmies, attackerDices)) {
						System.out.println("Enter the Defender command?");
						String defenderUserCommand = br.readLine().trim();
						if (checkUserDefenderValidation(defenderUserCommand)) {
							String[] defenderDetails = defenderUserCommand.split(" ");
							int defenderDices = Integer.parseInt(defenderDetails[1]);
							int defenderArmies = defenderCountryObject.getNoOfArmies();
							if (isDefenderDicePossible(defenderArmies, defenderDices)) {
								attackingStarted(attackerDices, defenderDices, attackCountryObject,
										defenderCountryObject);
								if (isAttackerWon(defenderCountryObject)) {
									moveArmyToConquredCountry(playersList, player, attackCountryObject,
											defenderCountryObject);
								}
							} else {
								reasonForFailedDefender(defenderArmies, defenderDices);
							}
						} else {
							System.out.println("Please enter the defender Command in the below correct Format\n"
									+ "Format :defend numdice[numdice>0]\n");
						}
					} else {
						reasonForFailedAttack(attackerArmies, attackerDices);
					}
				} else if (errorOccured == true) {
					System.out.println(errorDetails);
					errorDetails = "";
					errorOccured = false;
				}
			} else {
				System.out.println("Please enter the attack Command in any one of the below correct Format\n"
						+ "Format 1:attack countrynamefrom countynameto numdice[numdice>0]\n"
						+ "Format 2:attack countrynamefrom countynameto  –allout\n" + "Format 3:attack -noattack\n");
			}
			System.out.println("Do you want to attack again? Yes/No");
			String continueAttacking = br.readLine().trim();
			while (!(continueAttacking.equalsIgnoreCase("Yes") || continueAttacking.equalsIgnoreCase("No")
					|| continueAttacking == null)) {
				System.err.println("\nPlease enter the choice as either Yes or No:");
				continueAttacking = br.readLine().trim();
			}
			if (continueAttacking.equalsIgnoreCase("Yes")) {
				gameContinue = true;
			} else {
				gameContinue = false;
				System.out.println("Attacking Phase is ended");
				System.exit(0);
			}
		} while (gameContinue);
	}

	// First Start//

	public boolean isCountryPresent(String currentCountry, GameMap mapDetails) {
		for (int i = 0; i < mapDetails.getCountries().size(); i++) {
			if (mapDetails.getCountries().get(i).getCountryName().toString().equalsIgnoreCase(currentCountry)) {
				return true;
			}
		}
		return false;
	}

	public boolean isCountryAttackPresent(GamePlayer player, String currentCountry, GameMap mapDetails) {
		for (int i = 0; i < mapDetails.getCountries().size(); i++) {
			if (mapDetails.getCountries().get(i).getCountryName().toString().equalsIgnoreCase(currentCountry)) {
				if (mapDetails.getCountries().get(i).getPlayer().equalsIgnoreCase(player.getPlayerName())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isCountryAdjacent(Country attackCountryObject, String defenderCountry, GameMap mapDetails) {

		ArrayList<String> neighbourCountires = attackCountryObject.getNeighbourCountries();
		for (int i = 0; i < neighbourCountires.size(); i++) {
			if (neighbourCountires.get(i).equalsIgnoreCase(defenderCountry)) {
				return true;
			}
		}
		return false;
	}

	public boolean isDefenderDicePossible(int DefenderArmies, int defenderDices) {
		if (defenderDices <= 2 && defenderDices <= DefenderArmies) {
			return true;
		}
		return false;
	}

	public boolean isAttackerDicePossible(int AttackerArmies, int attackDices) {

		if (attackDices <= 3 && attackDices <= AttackerArmies - 1) {
			return true;
		}
		return false;
	}

	public boolean ableToMoveArmy(Country attackCountryObject, int moveNumberOfArmies) {
		if (moveNumberOfArmies <= 0) {
			return false;
		} else if (attackCountryObject.getNoOfArmies() - 1 >= moveNumberOfArmies) {
			return true;
		}
		return false;
	}

	public boolean isAttackerWon(Country defenderCountryObject) {
		if (defenderCountryObject.getNoOfArmies() == 0) {
			return true;
		}
		return false;
	}
	// First End//

	// Second Start//
	public Country getCountryObject(String currentCountry, GameMap mapDetails) {
		Country attackCountryObject = null;
		for (int i = 0; i < mapDetails.getCountries().size(); i++) {
			if (mapDetails.getCountries().get(i).getCountryName().toString().equalsIgnoreCase(currentCountry)) {
				attackCountryObject = mapDetails.getCountries().get(i);
			}
		}
		return attackCountryObject;
	}

	public List<Integer> outComesOfDices(int noOfDices) {
		List<Integer> outComes = new ArrayList<Integer>();
		for (int i = 0; i < noOfDices; i++) {
			Random random = new Random();
			int result = random.nextInt(5) + 1;
			outComes.add(result);
		}
		return outComes;
	}

	public int minimumBattles(int attackerDices, int defenderDices) {
		if (attackerDices < defenderDices) {
			return attackerDices;
		} else if (attackerDices > defenderDices) {
			return defenderDices;
		} else {
			return defenderDices;
		}
	}
	// Second End//

	// Third Start//

	public void reasonForFailedAttack(int attackerArmies, int attackerDices) {
		if (attackerArmies == 1) {
			System.out.println("Attacking is not possible.As the Attacking Country has only 1 Army");
		} else if (attackerDices > 3) {
			System.out.println("Attacking Dice cannot be more than 3");
		} else {
			System.out.println(attackerArmies - 1 + " Attacking Armies should be more than or equal to Attacking Dices"
					+ attackerDices);
		}
	}

	public void reasonForFailedDefender(int defenderArmies, int defenderDices) {
		if (defenderArmies == 0) {
			System.out.println("Defending is not possible.As the Defending Country has 0 Army");
		} else if (defenderArmies > 2) {
			System.out.println("Defending Army should be less than or equal to 2");
		} else {
			System.out.println(defenderArmies + " Defending Armies should be more than or equal to Defending Dices"
					+ defenderDices);
		}
	}

	// Third End//

	// Forth Start//

	public int maxAllowableAttackerDice(int attackerArmies) {
		if (attackerArmies >= 3) {
			return 3;
		} else {
			return attackerArmies - 1;
		}
	}

	public int maxAllowableDefenderDice(int DefenderArmies) {
		if (DefenderArmies >= 2) {
			return 2;
		} else {
			return DefenderArmies;
		}
	}
	// Forth End//

	// Fifth Start//
	public void attackingStarted(int attackerDices, int defenderDices, Country attackCountryObject,
			Country defenderCountryObject) {
		List<Integer> attackerOutcomes = outComesOfDices(attackerDices);
		List<Integer> defenderOutcomes = outComesOfDices(defenderDices);
		Collections.sort(attackerOutcomes);
		Collections.reverse(attackerOutcomes);
		Collections.sort(defenderOutcomes);
		Collections.reverse(defenderOutcomes);
		int battles = minimumBattles(attackerDices, defenderDices);
		for (int i = 0; i < battles; i++) {
			System.out.println("Battle :" + i);
			System.out.println("Attacker value is: " + attackerOutcomes.get(i));
			System.out.println("Defender value is: " + defenderOutcomes.get(i));
			if (attackerOutcomes.get(i) > defenderOutcomes.get(i)) {
				System.out.println("Attacker won the battle");
				defenderCountryObject.setNoOfArmies(defenderCountryObject.getNoOfArmies() - 1);
			} else {
				System.out.println("Defender won the battle");
				attackCountryObject.setNoOfArmies(attackCountryObject.getNoOfArmies() - 1);
			}
		}
	}

	public void moveArmyToConquredCountry(ArrayList<GamePlayer> playersList, GamePlayer player,
			Country attackCountryObject, Country defenderCountryObject) throws IOException {
		String choice = "";
		do {
			try {
				System.out.println("Enter the Attack Move command? " + " Maximum allowable Armies to move is: "
						+ (attackCountryObject.getNoOfArmies() - 1));
				String userCommand = br.readLine().trim();
				String[] attackMoveDetails = userCommand.split(" ");
				int moveNumberOfArmies = Integer.parseInt(attackMoveDetails[1]);
				if (checkUserAttackMoveValidation(userCommand)
						&& ableToMoveArmy(attackCountryObject, moveNumberOfArmies)) {
					removeOwnerAddNewOwner(playersList, player, defenderCountryObject.getCountryName());

					defenderCountryObject.setPlayer(attackCountryObject.getPlayer());
					defenderCountryObject.setNoOfArmies(moveNumberOfArmies);
					attackCountryObject.setNoOfArmies(attackCountryObject.getNoOfArmies() - moveNumberOfArmies);
					choice = "";
				} else {
					if (!checkUserAttackMoveValidation(userCommand)) {
						System.out.println("Please enter the Attack Move Command in the below correct Format\n"
								+ "Format :attackmove num[num>0]\n");
					} else if (!ableToMoveArmy(attackCountryObject, moveNumberOfArmies)) {
						System.out.println("It is not possible to move" + moveNumberOfArmies);
					}
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					System.out.println(
							"Do you want to still move the Army? Yes/No[Incase of 'No' By default 1 Army will be moved to the conqured Country");
					choice = br.readLine().trim();
				}
			} catch (Exception ex) {
				System.out.println("Please enter the Attack Move Command in the below correct Format\n"
						+ "Format :attackmove num[num>0]\n");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println(
						"Do you want to still move the Army? Yes/No[Incase of 'No' By default 1 Army will be moved to the conqured Country");
				choice = br.readLine().trim();
			}

			if (choice.equalsIgnoreCase("No")) {
				removeOwnerAddNewOwner(playersList, player, defenderCountryObject.getCountryName());

				defenderCountryObject.setPlayer(attackCountryObject.getPlayer());
				defenderCountryObject.setNoOfArmies(1);
				attackCountryObject.setNoOfArmies(attackCountryObject.getNoOfArmies() - 1);
				choice = "";
			}
		} while (choice.equalsIgnoreCase("Yes"));
	}

	// Fifth End//

	public boolean isAllOut(Country attackCountryObject, Country defenderCountryObject) {
		return true;
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

	public boolean checkUserValidation(String userCommand) {
		String[] attackDetails = userCommand.split(" ");
		int attackChoice = attackDetails.length;
		boolean validationOfUserCommand = true;
		switch (attackChoice) {
		case 2:
			validationOfUserCommand = checkNoAttackCommand(attackDetails);
			break;
		case 4:
			validationOfUserCommand = checkAttackCommand(attackDetails);
			break;
		default:
			validationOfUserCommand = false;
			break;
		}
		return validationOfUserCommand;
	}

	public boolean checkNoAttackCommand(String[] attackDetails) {
		String firstString = attackDetails[0];
		String secondString = attackDetails[1];
		if (firstString.equalsIgnoreCase("attack") && secondString.equalsIgnoreCase("-noattack")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkAllOutCommand(String[] attackDetails) {
		String firstString = attackDetails[0];
		String fourthString = attackDetails[3];
		if (firstString.equalsIgnoreCase("attack") && fourthString.equalsIgnoreCase("-allout")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkSingleAttackCommand(String[] attackDetails) {
		try {
			String firstString = attackDetails[0];
			int diceValue = Integer.parseInt(attackDetails[3]);
			if (firstString.equalsIgnoreCase("attack") && diceValue >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean checkAttackCommand(String[] attackDetails) {
		String thridString = attackDetails[3];
		boolean validationOfUserCommand = true;
		if (thridString.equalsIgnoreCase("-allout")) {
			validationOfUserCommand = checkAllOutCommand(attackDetails);
		} else {
			validationOfUserCommand = checkSingleAttackCommand(attackDetails);

		}
		return validationOfUserCommand;
	}

	public boolean checkUserDefenderValidation(String userCommand) {
		String[] defenderDetails = userCommand.split(" ");
		int defenderChoice = defenderDetails.length;
		boolean validationOfUserCommand = true;
		switch (defenderChoice) {
		case 2:
			validationOfUserCommand = checkDefenderCommand(defenderDetails);
			break;
		default:
			validationOfUserCommand = false;
			break;
		}
		return validationOfUserCommand;
	}

	public boolean checkDefenderCommand(String[] defenderDetails) {
		try {
			String firstString = defenderDetails[0];
			int diceValue = Integer.parseInt(defenderDetails[1]);
			if (firstString.equalsIgnoreCase("defend") && diceValue >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean checkUserAttackMoveValidation(String userCommand) {
		String[] attackMoverDetails = userCommand.split(" ");
		int defenderChoice = attackMoverDetails.length;
		boolean validationOfUserCommand = true;
		switch (defenderChoice) {
		case 2:
			validationOfUserCommand = checkAttackMoveCommand(attackMoverDetails);
			break;
		default:
			validationOfUserCommand = false;
			break;
		}
		return validationOfUserCommand;
	}

	public boolean checkAttackMoveCommand(String[] attackMoverDetails) {
		try {
			String firstString = attackMoverDetails[0];
			int diceValue = Integer.parseInt(attackMoverDetails[1]);
			if (firstString.equalsIgnoreCase("attackmove") && diceValue >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public void removeOwnerAddNewOwner(ArrayList<GamePlayer> playersList, GamePlayer player, String countryName) {
		// 1 Country owner list is removed with the lost country
		Country conquredCountry = null;
		for (int i = 0; i < playersList.size(); i++) {
			GamePlayer currentPlayer = playersList.get(i);
			for (int j = 0; j < currentPlayer.getPlayerCountries().size(); j++) {

				String currentCountryName = currentPlayer.getPlayerCountries().get(j).getCountryName();
				if (currentCountryName.equalsIgnoreCase(countryName)) {
					conquredCountry = currentPlayer.getPlayerCountries().get(j);
					currentPlayer.getPlayerCountries().remove(j);
					break;
				}
			}
		}
		// 2 Country owner list is updated with the conquered country

		if (conquredCountry != null) {
			player.getPlayerCountries().add(conquredCountry);
		}

	}

}