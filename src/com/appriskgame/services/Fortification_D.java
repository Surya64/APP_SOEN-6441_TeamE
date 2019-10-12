package com.appriskgame.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.RiskPlayer;

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
public class FortificationPhase {
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

	public void moveArmies(Country fromCountry, Country toCountry, int armiesCount) {

		boolean adjacentCountries = false;
		for (String country : fromCountry.getNeighbourCountriesToAdd()) {
			if (country.equalsIgnoreCase(toCountry.getCountryName())) {
				int fromCountryArmy = fromCountry.getNoOfArmies();
				int toCountryArmy = toCountry.getNoOfArmies();
				fromCountry.setNoOfArmies(fromCountryArmy - armiesCount);
				toCountry.setNoOfArmies(toCountryArmy + armiesCount);
				adjacentCountries = true;
				doFortification = false;
				System.out.println("\nArmies successfully moved!");
				System.out.println("\nFortification phase ends!");
				break;
			}
		}

		if (!adjacentCountries) {
			System.out.println("Countries are not adjacent!");
			doFortification = true;
		}
	}

	
}

