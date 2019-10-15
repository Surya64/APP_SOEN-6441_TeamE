package com.appriskgame.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Country class is for all the details about a country
 *
 * @author Sai
 * @author Shruthi
 *
 */
public class Country {

	private String countryName;


	private String continentName;


	private ArrayList<String> neighbourCountries=new ArrayList<String>();;
	private List<Country> neighbourCountriesToAdd = new ArrayList<Country>();


	private Continent partOfContinent;
	private int armiesCount;
	private String player;

	/**
	 * To get the CountryName
	 *
	 * @return countryName
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * To set the CountryName
	 *
	 * @param countryName
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}


	/**
	 * To get the Continent name of the respective country
	 *
	 * @return continentName
	 */
	public String getContinentName() {
		return continentName;
	}

	/**
	 * To set the Continent name of the respective country
	 *
	 * @param continentName
	 */
	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	/**
	 * To get the Neighbor Countries To Add
	 *
	 * @return neighbourCountriesToAdd
	 */
	public List<Country> getNeighbourCountriesToAdd() {
		return neighbourCountriesToAdd;
	}

	/**
	 * To set the Neighbor Countries
	 *
	 * @param neighbourCountriesToAdd
	 */
	public void setNeighbourCountriesToAdd(List<Country> neighbourCountriesToAdd) {
		this.neighbourCountriesToAdd = neighbourCountriesToAdd;
	}

	/**
	 * To get the Number of armies for each country
	 *
	 * @return armiesCount
	 */
	public int getNoOfArmies() {
		return armiesCount;
	}

	/**
	 * To set the Number of countries for each country
	 *
	 * @param noOfArmies
	 */
	public void setNoOfArmies(int noOfArmies) {
		this.armiesCount = noOfArmies;
	}

	/**
	 * Get the list of adjacent countries.
	 *
	 * @return neighbourCountries
	 */
	public ArrayList<String> getNeighbourCountries() {
		return neighbourCountries;
	}

	/**
	 * Set the list of adjacent countries.
	 *
	 * @param neighbourCountries
	 */
	public void setNeighbourCountries(ArrayList<String> neighbourCountries) {
		this.neighbourCountries = neighbourCountries;
	}

	/**
	 * Get the continent object which country belongs to.
	 *
	 * @return partOfContinent
	 */
	public Continent getPartOfContinent() {
		return partOfContinent;
	}

	/**
	 * Set the continent object which country belongs to.
	 *
	 * @param partOfContinent
	 */
	public void setPartOfContinent(Continent partOfContinent) {
		this.partOfContinent = partOfContinent;
	}

	/**
	 * Get the player name to whom country belong to.
	 *
	 * @return player
	 */
	public String getPlayer() {
		return player;
	}

	/**
	 * Set the player name to whom country belong to.
	 *
	 * @param player
	 */
	public void setPlayer(String player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return "[Countryname=" + countryName + ", NeighbourCountries="
				+ neighbourCountries + ", NoOfArmies=" + armiesCount + ", Continent=" + continentName + ", Owner="
				+ player + "]\n";
	}

}
