package com.appriskgame.model;

import java.util.ArrayList;
import java.util.List;

public class Country {

	private String countryName;
	private String continentName;
	private int continentNumber;
	private ArrayList<String> neighbourCountries=new ArrayList<String>();;
	private List<Country> neighbourCountriesToAdd = new ArrayList<Country>();
	private Continent partOfContinent;
	private int armiesCount;
	private String player;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public List<Country> getneighbourCountriesToAdd() {
		return neighbourCountriesToAdd;
	}

	public void setneighbourCountriesToAdd(List<Country> neighbourCountriesToAdd) {
		this.neighbourCountriesToAdd = neighbourCountriesToAdd;
	}

	public String getContinentName() {
		return continentName;
	}

	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	public List<Country> getNeighbourCountriesToAdd() {
		return neighbourCountriesToAdd;
	}

	public void setNeighbourCountriesToAdd(List<Country> neighbourCountriesToAdd) {
		this.neighbourCountriesToAdd = neighbourCountriesToAdd;
	}

	public int getNoOfArmies() {
		return armiesCount;
	}

	public void setNoOfArmies(int noOfArmies) {
		this.armiesCount = noOfArmies;
	}

	/**
	 * Get the list of adjacent countries.
	 * 
	 * @return adjacent Countries
	 */
	public ArrayList<String> getNeighbourCountries() {
		return neighbourCountries;
	}

	public void setNeighbourCountries(ArrayList<String> neighbourCountries) {
		this.neighbourCountries = neighbourCountries;
	}

	/**
	 * Get the continent name to which country belongs to.
	 * 
	 * @return Name of the continent
	 */
	public Continent getPartOfContinent() {
		return partOfContinent;
	}

	public void setPartOfContinent(Continent partOfContinent) {
		this.partOfContinent = partOfContinent;
	}

	/**
	 * Get the player name to whom country belong to.
	 * 
	 * @return Name of the player
	 */
	public String getPlayer() {
		return player;
	}

	/**
	 * Set the player name to whom country belong to.
	 * 
	 * @param player name To set the name of the player
	 */
	public void setPlayer(String player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return "Country [name=" + countryName + ", partOfContinent=" + partOfContinent + ", adjacentCountries="
				+ neighbourCountries + ", noOfArmies=" + armiesCount + ", continent=" + continentNumber + ", player="
				+ player + "]";
	}

}
