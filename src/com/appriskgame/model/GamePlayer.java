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

}
