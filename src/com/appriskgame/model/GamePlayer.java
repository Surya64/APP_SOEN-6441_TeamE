package com.appriskgame.model;

import java.util.ArrayList;

/**
 * This class stores the value associated to each player. It stores player's
 * name of String type, total armyCount of player as Integer type, and the
 * ArrayList of type Country which the player owns.
 * 
 * @author Dolly
 * @author Sahana
 *
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

	public ArrayList<Country> getPlayerCountries() {
		return playerCountries;
	}

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
	 * @param country - The country that needs to be added.
	 */
	public void addCountry(Country country) {

		this.playerCountries.add(country);
	}

	/**
	 *
	 * This method check the army count entered by the user and if it is less than
	 * the available, it assigned to the mentioned country
	 *
	 * @param country     - the country given to players
	 * @param armiesCount - the count of the armies player has
	 */
	public void userAssignedArmiesToCountries(Country country, int armiesCount) {

		if (this.getPlayerCountries().contains(country)) {
			if ((this.getNoOfArmies()) > 0 && this.getNoOfArmies() >= armiesCount) {
				country.setNoOfArmies(country.getNoOfArmies() + armiesCount);
				this.setNoOfArmies(this.getNoOfArmies() - armiesCount);
			} else {
				System.out.println("Insufficient number of armies.\n");
			}
		} else {
			System.out.println("This country is not owned by you!\n");
		}
	}
	@Override
	public String toString() {
		return "Player [PlayerName=" + playerName + ", Armies=" + noOfArmies + ", Countries=" + playerCountries + "]";

	}

	public String countryName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object Country() {
		// TODO Auto-generated method stub
		return null;
	}

}
