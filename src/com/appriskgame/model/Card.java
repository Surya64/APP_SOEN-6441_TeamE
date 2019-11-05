package com.appriskgame.model;

/**
 * Card definition, to have different types of cards
 *
 * @author Shruthi
 *
 */

public class Card {

	private final String type;
	private final Country country;

	/**
	 * this constructor method assigning the country and the card type
	 *
	 * @param type    card type
	 * @param country name of the country
	 */
	public Card(String type, Country country) {
		this.type = type;
		this.country = country;
	}

	/**
	 * This method returns the country name and card type
	 *
	 * @return name of country and type
	 */
	public String getName() {
		return country.getCountryName() + ", " + type;
	}

	/**
	 * This method returns type of the card
	 *
	 * @return string card object
	 */
	public String getType() {
		return type;
	}

	/**
	 * This method returns name of the country
	 *
	 * @return string country object
	 */
	public Country getCountry() {
		return country;
	}
}