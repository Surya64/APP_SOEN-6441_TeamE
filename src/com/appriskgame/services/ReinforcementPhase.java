package com.appriskgame.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

/**
 * Reinforcement has methods required for reinforcing the armies to each player
 * in their turn. Based on certain conditions players will be getting some
 * number of armies before the attack begins
 *
 * @author Shruthi
 *
 */
public class ReinforcementPhase {

	public static ArrayList<Country> listOfContries = new ArrayList<Country>();
	public static String playersChoice;
	public static List<String> playersChoiceList = new ArrayList<String>();

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
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		ArrayList<Country> numOfcontries = mapDetails.getCountries();
		for (int i = 0; i < numOfcontries.size(); i++) {
			Country country = numOfcontries.get(i);
			if (country.getPartOfContinent().getContinentName().equalsIgnoreCase(playerContinent.getContinentName())) {
				listOfContries.add(country);
			}
		}

		int NumOfReinforcementArmies = assignReinforcedArmies(player, playerContinent);
		System.out.println("Armies available for Reinforcement: " + NumOfReinforcementArmies);
		System.out.println("Please enter the country and number of armies to reinforce");

		playersChoice = input.readLine().trim();
		playersChoiceList = Arrays.asList(playersChoice.split(" "));
		if (!(playersChoiceList.size() == 3)) {
			System.out.println("Please enter the right format like : reinforce countryname num");
			playersChoice = input.readLine().trim();
			playersChoiceList = Arrays.asList(playersChoice.split(" "));
		}
		String countryName = playersChoiceList.get(1);
		String armyCount = playersChoiceList.get(2);
//        if (!myCountries.stream().anyMatch(countryName::equalsIgnoreCase))
		Pattern numberPattern = Pattern.compile("[0-9+]");
		Matcher match = numberPattern.matcher(armyCount);
		while (!match.matches() || armyCount.isEmpty() || !player.getPlayerCountries().contains(countryName)) {
			if (!player.getPlayerCountries().contains(countryName)) {
				System.out.println("Please enter the country that you own in right format");
				playersChoice = input.readLine().trim();
				playersChoiceList = Arrays.asList(playersChoice.split(" "));
				if (!(playersChoiceList.size() == 3)) {
					System.out.println("Please enter the right format like : reinforce countryname num");
					playersChoice = input.readLine().trim();
					playersChoiceList = Arrays.asList(playersChoice.split(" "));
				}
				countryName = playersChoiceList.get(1);
				armyCount = playersChoiceList.get(2);
			}

			if (!match.matches() || armyCount.isEmpty()) {
				System.out.println("\nPlease enter the correct army count in right format");
				playersChoice = input.readLine().trim();
				playersChoiceList = Arrays.asList(playersChoice.split(" "));
				if (!(playersChoiceList.size() == 3)) {
					System.out.println("Please enter the right format like : reinforce countryname num");
					playersChoice = input.readLine().trim();
					playersChoiceList = Arrays.asList(playersChoice.split(" "));
				}

				countryName = playersChoiceList.get(1);
				armyCount = playersChoiceList.get(2);
				match = numberPattern.matcher(armyCount);
			}
		}
		int numOfarmies = Integer.parseInt(armyCount);
		for (Country country : player.getPlayerCountries()) {

			if (country.getCountryName().equalsIgnoreCase(countryName)) {
				player.userAssignedArmiesToCountries(country, numOfarmies);
			}

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

	public static int assignReinforcedArmies(GamePlayer player, Continent continent) {
		int contriesPlyerOwns = player.getPlayerCountries().size();
		int reinformentArmiesAssigned;

		if (contriesPlyerOwns >= 9) {
			reinformentArmiesAssigned = (int) Math.floor(contriesPlyerOwns / 3);
		} else {
			reinformentArmiesAssigned = 3;
		}
		if (doesPlayerOwnAContinent(player, listOfContries))
			reinformentArmiesAssigned = continent.getContinentControlValue();

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

}
