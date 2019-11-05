package com.appriskgame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class allows the creation of deck of risk cards
 *
 * @author Shruthi
 *
 */
public class Deck implements Serializable {

	private static Deck deck;

	private String[] typeOfCards;
	public ArrayList<Card> deckOfCards;
	private Card drawCard;

	/**
	 * Creates all cards, one for each territory. Each card has either a type of
	 * Infantry, Cavalry, or Artillery.
	 * 
	 * @param list list of country names
	 */
	public void setDeckOfCards(List<Country> listOfCountries) {

		Collections.shuffle(listOfCountries);

		typeOfCards = new String[] { "Infantry", "Cavalry", "Artillery" };

		deckOfCards = new ArrayList<Card>();

		for (int i = 0; i < listOfCountries.size(); i++) {
			// Adding new instance of cards to deck
			deckOfCards.add(new Card(typeOfCards[i % 3], listOfCountries.get(i)));
			// System.out.println("Added new card to deck: " + deck.get(i).getName());
		}
		Collections.shuffle(deckOfCards);
	}

	/**
	 * To get the instance of the Deck
	 * 
	 * @return It is returning of type deck
	 */
	public static Deck getInstance() {
		if (null == deck) {
			deck = new Deck();
		}
		return deck;
	}

	/**
	 * Removing a card from the deck and returning it
	 * 
	 * @return card object
	 */
	public Card draw() {

		drawCard = deckOfCards.get(0);
		deckOfCards.remove(0);

		return drawCard;
	}

	/**
	 * This method adds a card to the deck
	 * 
	 * @param card object of the card which is to be added to deck
	 */
	public void add(Card card) {

		deckOfCards.add(card);
	}

	/**
	 * This method shuffles the deck of cards
	 */
	public void shuffle() {

		Collections.shuffle(deckOfCards);
	}

}
