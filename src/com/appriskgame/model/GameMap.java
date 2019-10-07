package com.appriskgame.model;

import java.util.ArrayList;

public class GameMap {

	private ArrayList<Country> countries = new ArrayList<Country>();
	private ArrayList<Continent> continents = new ArrayList<Continent>();

	public ArrayList<Continent> getContinents() {
		return continents;
	}

	public void setContinents(ArrayList<Continent> continents) {
		this.continents = continents;
	}

	public ArrayList<Country> getCountries() {
		return countries;
	}

	public void setCountries(ArrayList<Country> countries) {
		this.countries = countries;
	}

}
