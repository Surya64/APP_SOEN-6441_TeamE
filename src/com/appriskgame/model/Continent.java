package com.appriskgame.model;

import java.util.ArrayList;
import java.util.List;

public class Continent {

	private String continentName;
	private int continentControlValue;
	private List<Country> listOfCountries = new ArrayList<Country>();

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

	public List<Country> getListOfCountries() {
		return listOfCountries;
	}

	public void setListOfCountries(List<Country> listOfCountries) {
		this.listOfCountries = listOfCountries;
	}

}
