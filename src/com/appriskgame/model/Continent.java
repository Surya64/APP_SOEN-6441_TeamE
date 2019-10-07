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

<<<<<<< HEAD
}
=======
	@Override
	public String toString() {
		return "Continents: \n[continentName=" + continentName + ", controlValue=" + continentControlValue
				+ ", listOfCountries=" + listOfCountries + "]/n";
	}

}
>>>>>>> d80079d83a8e2a46d46a48cbc62a5c9691927e31
