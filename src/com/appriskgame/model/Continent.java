package com.appriskgame.model;

import java.util.ArrayList;

public class Continent {

	private String continentName;
	private int continentControlValue;
	private ArrayList<Country> listOfCountries = new ArrayList<Country>();

	public String getContinentName() {
		return continentName;
	}

	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	public int getContinentControlValue() {
		return continentControlValue;
	}

	public void setContinentControlValue(int continentControlValue) {
		this.continentControlValue = continentControlValue;
	}

	public ArrayList<Country> getListOfCountries() {
		return listOfCountries;
	}

	public void setListOfCountries(ArrayList<Country> listOfCountries) {
		this.listOfCountries = listOfCountries;
	}

	@Override
	public String toString() {
		return "Continents: \n[continentName=" + continentName + ", controlValue=" + continentControlValue
				+ ", listOfCountries=" + listOfCountries + "]/n";
	}

}
