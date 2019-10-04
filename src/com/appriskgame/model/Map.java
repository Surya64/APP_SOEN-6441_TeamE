package com.appriskgame.model;

import java.util.ArrayList;
import java.util.List;

public class Map {

	private List<Country> countries = new ArrayList<Country>();
	private List<Continent> continents = new ArrayList<Continent>();

	public List<Continent> getContinents() {
		return continents;
	}

	public void setContinents(List<Continent> continents) {
		this.continents = continents;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

}
