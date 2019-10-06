package com.appriskgame.model;

import java.util.ArrayList;
import java.util.List;

public class Country {

	private String countryName;
	private String continentName;

	private List<Country> neighbourCountriesToAdd  = new ArrayList<Country>();

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public List<Country> getneighbourCountriesToAdd () {
		return neighbourCountriesToAdd ;
	}

	public void setneighbourCountriesToAdd (List<Country> neighbourCountriesToAdd ) {
		this.neighbourCountriesToAdd  = neighbourCountriesToAdd ;
	}

	public String getContinentName() {
		return continentName;
	}

	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

}