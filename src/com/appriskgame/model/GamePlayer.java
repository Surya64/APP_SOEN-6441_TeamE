package com.appriskgame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * This class stores the value associated to each player. It stores player's
 * name of String type, total armyCount of player as Integer type, and the
 * ArrayList of type Country which the player owns.
 *
 * @author Dolly
 * @author Sahana
 */
public class GamePlayer extends Observable implements Serializable {

	private String playerName;
	private ArrayList<Country> playerCountries = new ArrayList<Country>();
	private int noOfArmies = 0;
	private int NumOfTypeCardsExchanged = 0;
	private String SameCardExchangeThrice = "";
	private List<Card> cardList = new ArrayList<>();
	private Deck deck = Deck.getInstance();
	private boolean exchanged = false;
	public int playerCardExchangeCount = 0;

	public int getPlayerCardExchangeCount() {
		return playerCardExchangeCount;
	}

	public void setPlayerCardExchangeCount(int playerCardExchangeCount) {
		this.playerCardExchangeCount = playerCardExchangeCount;
	}

	public boolean isExchanged() {
		return exchanged;
	}

	public void setExchanged(boolean exchanged) {
		this.exchanged = exchanged;
	}

	/**
	 * This method returns the number of type of cards
	 *
	 * @return NumOfTypeCardsExchanged number of type of cards
	 */
	public int getNumOfTypeCardsExchanged() {
		return NumOfTypeCardsExchanged;
	}

	/**
	 * This method sets the number of type of cards
	 *
	 * @param numOfTypeCardsExchanged number of type of cards
	 */
	public void setNumOfTypeCardsExchanged(int numOfTypeCardsExchanged) {
		NumOfTypeCardsExchanged = numOfTypeCardsExchanged;
	}

	/**
	 * This method returns card which appear more than thrice during exchange
	 *
	 * @return SameCardExchangeThrice card which appear more that thrice in exchange
	 */
	public String getSameCardExchangeThrice() {
		return SameCardExchangeThrice;
	}

	/**
	 * This method sets the number of cards which appear more than thrice during
	 * exchange
	 *
	 * @param sameCardExchangeThrice card which appear more that thrice in exchange
	 */
	public void setSameCardExchangeThrice(String sameCardExchangeThrice) {
		SameCardExchangeThrice = sameCardExchangeThrice;
	}

	/**
	 * This method returns the list of cards player has
	 *
	 * @return cardList list of cards
	 */
	public List<Card> getCardList() {
		return cardList;
	}

	/**
	 * This method sets the list of cards player has
	 *
	 * @return cardList list of cards
	 */
	public void setCardList(List<Card> cardList) {
		this.cardList = cardList;
	}

	/**
	 * Get the Player name.
	 *
	 * @return Name of the player
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Set the Player name.
	 *
	 * @param name To set the name of the Player
	 */
	public void setPlayerName(String name) {
		this.playerName = name;
	}

	/**
	 * Get the list of the countries assigned to player.
	 *
	 * @return Countries List of countries of player .
	 */
	public ArrayList<Country> getPlayerCountries() {
		return playerCountries;
	}

	/**
	 * Set the list of the countries to player.
	 *
	 * @param countries To set the list of the countries to player.
	 */
	public void setPlayerCountries(ArrayList<Country> countries) {
		this.playerCountries = countries;
	}

	/**
	 * Get the army count of the player.
	 *
	 * @return Army count
	 */
	public int getNoOfArmies() {
		return noOfArmies;
	}

	/**
	 * Set the army count of the player.
	 *
	 * @param count To set the army count of the player
	 */
	public void setNoOfArmies(int count) {
		this.noOfArmies = count;
	}

	/**
	 * This method is used to add the country to the player's countries list.
	 *
	 * @param country The country that needs to be added.
	 */
	public void addCountry(Country country) {
		this.playerCountries.add(country);
	}

	@Override
	public String toString() {
		return "Player [PlayerName=" + playerName + ", Armies=" + noOfArmies + ", Countries=" + playerCountries + "]";

	}

	/**
	 * Checks if the given three types of cards are same
	 *
	 * @param exchangeCards       exchange cards
	 * @param cardAppearingThrice Card's name which is appearing thrice
	 * @return true if all are same else false
	 */
	public boolean checkCardSameType(List<Card> exchangeCards, String cardAppearingThrice) {
		int cardAppearingCount = 0;
		if (exchangeCards.size() < 3) {
			return false;
		} else {
			for (Card card : exchangeCards) {
				if (card.getType().equals(cardAppearingThrice)) {
					cardAppearingCount++;
				}
			}
			if (cardAppearingCount == 3) {
				SameCardExchangeThrice = cardAppearingThrice;
				return true;
			}
		}
		return false;
	}

	/**
	 * To check exchanging cards are of different types
	 *
	 * @param exchangeCards exchange cards
	 * @param cardTypes     card types
	 * @return true or false depending on the types of cards
	 */
	public boolean checkDiffTypesOfCards(List<Card> exchangeCards, int cardTypes) {
		if (cardTypes < 3) {
			return false;
		}
		List<String> types = new ArrayList<>();
		for (Card card : exchangeCards) {
			types.add(card.getType());
		}
		if (!types.get(0).equals(types.get(1)) && !types.get(1).equals(types.get(2))
				&& !types.get(2).equals(types.get(0))) {
			NumOfTypeCardsExchanged = 3;
			return true;
		}
		return false;
	}

	/**
	 * This method helps to get the cards from the player's list and add to the deck
	 * of cards
	 *
	 * @param cardTypes                   Type of cards
	 * @param cardAppearingMoreThanThrice Card name appearing thrice
	 * @param player                      Gameplayer object
	 * @param cardNumbers                 the cardNumbers
	 * @throws Exception exception of method
	 */
	public void exchangeCards(int cardTypes, String cardAppearingMoreThanThrice, GamePlayer player,
			List<Integer> cardNumbers) throws Exception {

		if (cardTypes == 3 || (cardAppearingMoreThanThrice != null && !cardAppearingMoreThanThrice.isEmpty())) {
			Card card1 = player.getCardList().get(cardNumbers.get(0) - 1);
			Card card2 = player.getCardList().get(cardNumbers.get(1) - 1);
			Card card3 = player.getCardList().get(cardNumbers.get(2) - 1);
			player.getCardList().remove(card1);
			player.getCardList().remove(card2);
			player.getCardList().remove(card3);

			deck.add(card1);
			deck.add(card2);
			deck.add(card3);
			setExchanged(true);
			setCardList(player.getCardList());
			setChanged();
			notifyObservers(player);
		}
	}

	/**
	 * If player has the card territory, he will get the extra set of armies
	 *
	 *
	 * @param exchangeCards list of cards
	 * @param player        GamePlayer object
	 * @return playerObject GamePlayer Object
	 */
	public GamePlayer exChangeCardTerritoryExist(List<Card> exchangeCards, GamePlayer player) {

		GamePlayer playerObject = new GamePlayer();
		playerObject = player;

		ArrayList<Country> countryList = new ArrayList<Country>();
		countryList = player.getPlayerCountries();

		ArrayList<Country> updatedCountryList = new ArrayList<Country>();

		System.out.println("Inside Exchange card Territory");

		for (Country country : countryList) {
			for (Card card : exchangeCards) {
				String[] CountryNameInCard = card.getName().split(",");
				String countryName = CountryNameInCard[0];
				if (countryName.equalsIgnoreCase(country.getCountryName())) {
					System.out
							.println("country name for the exchnage is :" + country.getCountryName());
					System.out
							.println("armies before exchnage in the Country is :" + country.getNoOfArmies());
					int NumOfarmies = country.getNoOfArmies();
					NumOfarmies = NumOfarmies + 2;
					country.setNoOfArmies(NumOfarmies);
					System.out.println("armies before exchnage in the Country is :" + country.getNoOfArmies());
				}
			}
			updatedCountryList.add(country);
		}

		playerObject.setPlayerCountries(updatedCountryList);
		return playerObject;

	}

}