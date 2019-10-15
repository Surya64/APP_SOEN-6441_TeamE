package com.appriskgame.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

/**
 * This class is dedicated for the fortification phase of the game. It
 * checks the number of countries user has proceeds if it is not less than 2.
 * Takes the fromCountry and toCountry value which identifies from where the
 * player wishes to move army and where. After performing requisite
 * validation moves army of the player.
 * 
 * @author Dolly
 * 
 */
public class Fortification_D {
	public boolean doFortification = false;

	/**
	 * This method is called from the Startup phase when the user opts to start the
	 * fortification. It internally calls the moveArmies method once all the
	 * validation with respect to fortification are performed.
	 * 
	 * @param player  - The player who is doing fortification.
	 * @param mapData - GameMapGraph object
	 * @throws IOException - throws Input-Output exception
	 */
	
	public void startGameFortification(GamePlayer player, GameMap mapData) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int countOfArmies = 0;
		if (player.getPlayerCountries().size() >= 2) {
			doFortification = true;
			String fromCountry = "";
			String toCountry = "";

			while (doFortification) {
				doFortification = true;
				Country givingCountry = new Country();
				Country receivingCountry = new Country();

				do {
					System.out.println("\nPlayer has the following list of countries with armies: \n");
					for (Country country : player.getPlayerCountries()) {
						System.out.println("* " + country.getCountryName() + ":" + country.getNoOfArmies() + "\n");
					}
					System.out.println("Enter the name of country from which you want to move armies :");
					fromCountry = br.readLine().trim().toUpperCase();
					Pattern namePattern1 = Pattern.compile("[a-zA-Z\\s]+");
					Matcher match = namePattern1.matcher(fromCountry);
					while (!match.matches() || fromCountry.isEmpty()) {
						System.out.println("\nPlease enter the correct country name below:");
						fromCountry = br.readLine().trim().toUpperCase();
						match = namePattern1.matcher(fromCountry);
					}

					System.out.println("Enter the name of country to which you want to move some armies, from country "
							+ fromCountry);
					toCountry = br.readLine().trim().toUpperCase();
					Pattern namePattern2 = Pattern.compile("[a-zA-Z\\s]+");
					match = namePattern2.matcher(toCountry);
					while (!match.matches() || toCountry.isEmpty()) {
						System.out.println("\nPlease enter the correct country name below:");

						toCountry = br.readLine().trim().toUpperCase();
						match = namePattern2.matcher(toCountry);
					}

					if (!mapData.getCountrySet().containsKey(fromCountry)
							|| !mapData.getCountrySet().containsKey(toCountry)) {
						doFortification = false;
						System.out.println("Country doesn't exist!\n");
					}

					givingCountry = mapData.getCountrySet().get(fromCountry);
					receivingCountry = mapData.getCountrySet().get(toCountry);
					if (player.getPlayerCountries().contains((CharSequence) givingCountry)
							&& player.getPlayerCountries().contains((CharSequence) receivingCountry)) {
						doFortification = true;
					} else {
						System.out.println(
								"Entered countries doesn't exist in player's owned country list, please enter country names again\n");
						doFortification = false;

					}
				} while (!doFortification);
				if (doFortification) {
					System.out.println("\nEnter the number of armies to move from " + fromCountry + " to " + toCountry);
					try {
						String countOfArmy = br.readLine().trim();
						Pattern numberPattern3 = Pattern.compile("[0-9]+");
						Matcher match = numberPattern3.matcher(countOfArmy);
						while (!match.matches() || countOfArmy.isEmpty()) {
							System.out.println("\nPlease enter the correct army count below:");

							countOfArmy = br.readLine().trim();
							match = numberPattern3.matcher(countOfArmy);
						}
						countOfArmies = Integer.parseInt(countOfArmy);
						if (countOfArmies > mapData.getCountrySet().get(fromCountry).getNoOfArmies()) {
							System.out.println(
									"Insufficient armies available, fortification is not possible with asked number of armies.");
							doFortification = false;
						}

					} catch (NumberFormatException e) {
						System.out.println("Invalid number of armies.");
					}
				}

				if (doFortification) {
					boolean fortify = false;
					for (Country country : player.getPlayerCountries()) {
						for (String temp  : country.getNeighbourCountries()) {
							if (temp.equalsIgnoreCase(fromCountry) || temp.equalsIgnoreCase(toCountry)) {
								fortify = true;
							}
						}
					}
					if (fortify) {
						moveArmies(givingCountry, receivingCountry, countOfArmies);
					} else {
						doFortification = false;
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
			if (((String) country).equalsIgnoreCase(toCountry.getCountryName())) {
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

