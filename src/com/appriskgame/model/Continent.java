package com.appriskgame.model;

import java.util.ArrayList;

/**
 * The Continent class is to have all the details regarding Continent
 *
 * @author Shruthi
 * @author Sahana
 *
 */
public class Continent {
	private String continentName;
	private int continentControlValue;
	private ArrayList<Country> listOfCountries = new ArrayList<Country>();

	/**
	 * Get the continent name.
	 *
	 * @return continent's Name
	 */
	public String getContinentName() {
		return continentName;
	}

	/**
	 * Set the continent name.
	 *
	 * @param name To set the name of the continent
	 */
	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	/**
	 * To get the Control value for each continent
	 *
	 * @return continentControlValue
	 */
	public int getContinentControlValue() {
		return continentControlValue;
	}

	/**
	 * To set the Control value for each continent
	 *
	 * @param continentControlValue
	 */
	public void setContinentControlValue(int continentControlValue) {
		this.continentControlValue = continentControlValue;
	}

	/**
	 * Get the list of countries in the particular continent.
	 *
	 * @return listOfContries
	 */
	public ArrayList<Country> getListOfCountries() {
		return listOfCountries;
	}

	/**
	 * Set the list of countries in the particular continent.
	 *
	 * @param listOfContries
	 */
	public void setListOfCountries(ArrayList<Country> listOfCountries) {
		this.listOfCountries = listOfCountries;
	}

	@Override
	public String toString() {
		return "Continents: \n[continentName=" + continentName + ", controlValue=" + continentControlValue
				+ ", listOfCountries=" + listOfCountries + "]/n";
	}

}