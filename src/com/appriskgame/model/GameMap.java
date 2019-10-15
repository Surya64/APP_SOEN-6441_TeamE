package com.appriskgame.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class stores the value associated to map.It holds the
 * values required to build the map.
 * 
 * @author Surya
 * @author Sahana
 * @author Sai
 *
 */
public class GameMap {
	/** ArrayList of Country objects to store the country details */
	private ArrayList<Country> countries = new ArrayList<Country>();

	/** ArrayList of Continent objects to store the continent details */
	private ArrayList<Continent> continents = new ArrayList<Continent>();

	/** HashMap for set of countries */
	private HashMap<String, Country> countrySet;

	/**
	 * Get the list of continents.
	 * 
	 * @return list of continents.
	 */
	public ArrayList<Continent> getContinents() {
		return continents;
	}
	/**
	 * Set the list of continents
	 * 
	 * @param continents To set the list of continents.
	 */
	public void setContinents(ArrayList<Continent> continents) {
		this.continents = continents;
	}
	/**
	 * Get the list of Countries.
	 * 
	 * @return list of Countries.
	 */
	public ArrayList<Country> getCountries() {
		return countries;
	}
	
	/**
	 * Set the list of Countries
	 * 
	 * @param countries To set the list of Countries.
	 */
	public void setCountries(ArrayList<Country> countries) {
		this.countries = countries;
	}
	
	/**
	 * Get the Country details.
	 * 
	 * @return Country details
	 */
	public HashMap<String, Country> getCountrySet() {
		return countrySet;
	}
	
	/**
	 * Set the Country details.
	 * 
	 * @param countrySet To set the Country details
	 */
	public void setCountrySet(HashMap<String, Country> countrySet) {
		this.countrySet = countrySet;
	}

	@Override
	public String toString() {
		return "[continents=" + continents + ", countries=" + countries + ", countrySet=" + countrySet + "]";
	}

}
