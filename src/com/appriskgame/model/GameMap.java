package com.appriskgame.model;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMap {

	private ArrayList<Country> countries = new ArrayList<Country>();
	private ArrayList<Continent> continents = new ArrayList<Continent>();
	private HashMap<String, Country> countrySet;

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

	public HashMap<String, Country> getCountrySet() {
		return countrySet;
	}

	public void setCountrySet(HashMap<String, Country> countrySet) {
		this.countrySet = countrySet;
	}
	@Override
	public String toString() {
		return "[continents="+ continents + ", countries=" + countries + ", countrySet=" + countrySet + "]";
	}

}
