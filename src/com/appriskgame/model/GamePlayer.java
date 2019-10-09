package com.appriskgame.model;

import java.util.ArrayList;

public class GamePlayer {
	private String playerName;
	private ArrayList<Country> playerCountries = new ArrayList<Country>();
	private int noOfArmies = 0;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String name) {
		this.playerName = name;
	}

	public ArrayList<Country> getPlayerCountries() {
		return playerCountries;
	}

	public void setPlayerCountries(ArrayList<Country> countries) {
		this.playerCountries = countries;
	}

	public int getNoOfArmies() {
		return noOfArmies;
	}

	public void setNoOfArmies(int count) {
		this.noOfArmies = count;
	}

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

}
