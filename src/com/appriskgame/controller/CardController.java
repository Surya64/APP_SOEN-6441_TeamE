package com.appriskgame.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.model.Card;
import com.appriskgame.model.Deck;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public class CardController {
	public void setDeckOfCards() {
		Deck.getInstance().setDeckOfCards();
	}

	/**
	 * This method allocates a card randomly to the player if the player wins the
	 * country
	 *
	 * @param player - the current player
	 */
	public void allocateCardToPlayer(GamePlayer player) {
		player.getCardList().add(Deck.getInstance().draw());
	}

	public int exchangeCards(GamePlayer player) throws Exception {

		HashMap<String, Integer> cardCount = new HashMap<>();
		int armiesInExchange = 0;
		Scanner input = new Scanner(System.in);
		int cardTypes = 0;
		boolean cardExchangePossible = false;
		String cardAppearingMoreThanThrice = null;

		if (player.getCardList().size() > 0) {
			for (Card card : player.getCardList()) {
				if (!cardCount.containsKey(card.getType())) {
					cardCount.put(card.getType(), 1);
					cardTypes++;
				} else {
					int c = cardCount.get(card.getType());
					c++;
					cardCount.put(card.getType(), c);
				}
				System.out.println(card.getType() + "with the CountryName:" + card.getCountry().getCountryName());
			}
		} else {
			System.out.println("Not enough cards to exchange ,continuing with the reinforcement phase");
		}

		if (cardTypes == 3) {
			cardExchangePossible = true;
		}

		for (Entry<String, Integer> cardVal : cardCount.entrySet()) {
			if (cardVal.getValue() >= 3) {
				cardExchangePossible = true;
				cardAppearingMoreThanThrice = cardVal.getKey();
			}
		}

		if (cardExchangePossible) {
			if (player.getCardList().size() < 5) {
				int i = 0;
				System.out.println("Available Cards to the player are");
				for (int k = 0; k < player.getCardList().size(); k++) {
					System.out.print(k + 1 + "." + player.getCardList().get(i).getType());
				}
				System.out.println(
						"Enter the numbers of card you want to exchange in the format exchangecards num num num");
				String[] cardsList = input.nextLine().split(" ");

				Pattern namePattern2 = Pattern.compile("exchangecards");
				Matcher match1 = namePattern2.matcher(cardsList[0]);

				Pattern numberPattern = Pattern.compile("[0-9]+");
				Matcher match2 = numberPattern.matcher(cardsList[1]);
				Matcher match3 = numberPattern.matcher(cardsList[2]);
				Matcher match4 = numberPattern.matcher(cardsList[3]);
				while (!match1.matches() || !match2.matches() || !match3.matches() || !match4.matches()) {
					System.out.println(
							"Enter the numbers of card you want to exchange in the format exchangecards num num num");
					cardsList = input.nextLine().split(" ");
					match1 = namePattern2.matcher(cardsList[0]);
					match2 = numberPattern.matcher(cardsList[1]);
					match3 = numberPattern.matcher(cardsList[2]);
					match4 = numberPattern.matcher(cardsList[3]);
				}
				List<Card> exchangeCards = new ArrayList<>();
				List<Integer> cardNumbers = new ArrayList<>();

				for (int k = 1; k < cardsList.length; k++) {
					cardNumbers.add(Integer.parseInt(cardsList[i]));
				}

				for (int c : cardNumbers) {
					exchangeCards.add(player.getCardList().get(c - 1));
				}

				if (!checkDiffTypesOfCards(exchangeCards, cardTypes)
						&& !checkCardSameType(exchangeCards, cardAppearingMoreThanThrice)) {
					System.out.println(
							"Please enter numbers of same cards appearing thrice or three cards which are different.");
					throw new Exception();
				}
				int count = GameMap.getCardExchangeCountinTheGame();
				armiesInExchange = (count + 1) * 5;
				GameMap.cardExchangeCountinTheGame = GameMap.getCardExchangeCountinTheGame() + 1;
				for (int c : cardNumbers) {
					player.getCardList().remove(c - 1);
				}
			}

			if (player.getCardList().size() >= 5) {
				int i = 0;
				System.out.println("Available Cards to the player are");
				for (int k = 0; k < player.getCardList().size(); k++) {
					System.out.print(k + 1 + "." + player.getCardList().get(i).getType());
				}

				System.out.println(
						"Enter the numbers of card you want to exchange in the format exchangecards num num num");
				String[] cardsList = input.nextLine().split(" ");

				Pattern namePattern2 = Pattern.compile("exchangecards");
				Matcher match1 = namePattern2.matcher(cardsList[0]);

				Pattern numberPattern = Pattern.compile("[0-9]+");
				Matcher match2 = numberPattern.matcher(cardsList[1]);
				Matcher match3 = numberPattern.matcher(cardsList[2]);
				Matcher match4 = numberPattern.matcher(cardsList[3]);
				while (cardsList.length < 4 || !match1.matches() || !match2.matches() || !match3.matches()
						|| !match4.matches()) {
					System.out.println(
							"Enter atleast 3 cards you want to exchange in the format exchangecards num num num");
					cardsList = input.nextLine().split(" ");
					match1 = namePattern2.matcher(cardsList[0]);
					match2 = numberPattern.matcher(cardsList[1]);
					match3 = numberPattern.matcher(cardsList[2]);
					match4 = numberPattern.matcher(cardsList[3]);
				}
				List<Card> exchangeCards = new ArrayList<>();
				List<Integer> cardNumbers = new ArrayList<>();

				for (int k = 1; k < cardsList.length; k++) {
					cardNumbers.add(Integer.parseInt(cardsList[i]));
				}

				for (int c : cardNumbers) {
					exchangeCards.add(player.getCardList().get(c - 1));
				}

				if (!checkDiffTypesOfCards(exchangeCards, cardTypes)
						&& !checkCardSameType(exchangeCards, cardAppearingMoreThanThrice)) {
					System.out.println(
							"Please enter numbers of same cards appearing thrice or three cards which are different.");
					throw new Exception();
				}
				int count = GameMap.getCardExchangeCountinTheGame();
				armiesInExchange = (count + 1) * 5;
				GameMap.cardExchangeCountinTheGame = GameMap.getCardExchangeCountinTheGame() + 1;
				for (int c : cardNumbers) {
					player.getCardList().remove(c - 1);
				}

			}
		}
		return armiesInExchange;
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
//			player.setNumOfTypeCardsExchanged(3);
			return true;
		}
		return false;
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
//				player.setSameCardExchangeThrice(cardAppearingThrice);
				return true;
			}
		}
		return false;
	}
}
