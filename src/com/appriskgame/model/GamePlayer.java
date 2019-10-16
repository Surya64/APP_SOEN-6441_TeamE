package com.appriskgame.model;

import java.util.ArrayList;

/**
 * This class stores the value associated to each player. It stores player's
 * name of String type, total armyCount of player as Integer type, and the
 * ArrayList of type Country which the player owns.
 * 
 * @author Dolly
 * @author Sahana
 */
public class GamePlayer {

	private String playerName;
	private ArrayList<Country> playerCountries = new ArrayList<Country>();
	private int noOfArmies = 0;

	/**
	 * Get the Player name.
	 * 
	 * @return Name of the player
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Set the Player name.
	 * 
	 * @param name To set the name of the Player
	 */
	public void setPlayerName(String name) {
		this.playerName = name;
	}

	/**
	 * Get the list of the countries assigned to player.
	 * 
	 * @return Countries List of countries of player .
	 */
	public ArrayList<Country> getPlayerCountries() {
		return playerCountries;
	}

	/**
	 * Set the list of the countries to player.
	 * 
	 * @param countries To set the list of the countries to player.
	 */
	public void setPlayerCountries(ArrayList<Country> countries) {
		this.playerCountries = countries;
	}

	/**
	 * Get the army count of the player.
	 * 
	 * @return Army count
	 */
	public int getNoOfArmies() {
		return noOfArmies;
	}

	/**
	 * Set the army count of the player.
	 * 
	 * @param armyCount To set the army count of the player
	 */
	public void setNoOfArmies(int count) {
		this.noOfArmies = count;
	}

	/**
	 * This method is used to add the country to the player's countries list.
	 * 
	 * @param country The country that needs to be added.
	 */
	public void addCountry(Country country) {
		this.playerCountries.add(country);
	}

	@Override
	public String toString() {
		return "Player [PlayerName=" + playerName + ", Armies=" + noOfArmies + ", Countries=" + playerCountries + "]";

	}
}
