package com.appriskgame.model;
import java.util.ArrayList;
import java.util.List;

public class Country {

	private String countryName;
	private int continentNumber;
	private int countryNumber;

	private List<Country> neighbourCountries = new ArrayList<Country>();

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getContinentNumber() {
		return continentNumber;
	}

	public void setContinentNumber(int continentNumber) {
		this.continentNumber = continentNumber;
	}

	public int getCountryNumber() {
		return countryNumber;
	}

	public void setCountryNumber(int countryNumber) {
		this.countryNumber = countryNumber;
	}

	public List<Country> getNeighbourCountries() {
		return neighbourCountries;
	}

	public void setNeighbourCountries(List<Country> neighbourCountries) {
		this.neighbourCountries = neighbourCountries;
	}

}
