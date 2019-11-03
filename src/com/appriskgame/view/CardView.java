package com.appriskgame.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.Scanner;

import com.appriskgame.model.Card;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

/**
 * This Class is to check the player has enough cards to exchange and for
 * observer pattern
 *
 * @author Shruthi
 *
 */

public class CardView implements Observer {

	public int exchangeCards(GamePlayer player) throws Exception {

		HashMap<String, Integer> cardCount = new HashMap<>();
		int armiesInExchange = 0;
		int cardTypes = 0;
		Scanner input = new Scanner(System.in);
		player.setPlayerCardExchangeCount(0);
		player.setSameCardExchangeThrice("");
		String cardExchangeChoice;
		boolean cardExchangePossible = false;
		String cardAppearingMoreThanThrice = null;
		System.out.println("The cards with this player are :");

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

		if (cardTypes == 3) {
			cardExchangePossible = true;
		}

		for (Entry<String, Integer> cardVal : cardCount.entrySet()) {
			if (cardVal.getValue() >= 3) {
				cardExchangePossible = true;
				cardAppearingMoreThanThrice = cardVal.getKey();
			}
		}

		if (player.getCardList().size() < 3) {
			System.out.println();
			System.out.println("Not enough cards to exchange ,continuing with the reinforcement phase");
		} else if (cardExchangePossible) {
			if (player.getCardList().size() < 5) {
				System.out.println("Do you want to exchange the cards to armies(yes/no");
				cardExchangeChoice = input.nextLine();
				if (cardExchangeChoice.equals("yes")) {
					int i = 0;
					for (Card card : player.getCardList()) {
						System.out.println(card.getType() + ", " + card.getCountry().getCountryName() + " : " + (++i));
					}
					System.out.println("Enter the numbers of card you want to exchange in comma seperated values");
					String[] cardsList = input.nextLine().split(" ");
					List<Card> exchangeCards = new ArrayList<>();
					List<Integer> cardNumbers = new ArrayList<>();
					for (String card : cardsList) {
						cardNumbers.add(Integer.parseInt(card));
					}
					if (cardNumbers.size() < 3) {
						System.out.println("Please enter at least 3 numbers for exchanging cards.");
						throw new Exception();
					}
					for (int c : cardNumbers) {
						exchangeCards.add(player.getCardList().get(c - 1));
					}
					if (!player.checkDiffTypesOfCards(exchangeCards, cardTypes)
							&& !player.checkCardSameType(exchangeCards, cardAppearingMoreThanThrice)) {
						System.out.println(
								"Please enter numbers of same cards appearing thrice or three cards which are different.");
						throw new Exception();
					}
					if (player.getPlayerCardExchangeCount() < 3 && (player.getSameCardExchangeThrice() == null
							|| player.getSameCardExchangeThrice().isEmpty())) {
						System.out.println(
								"Please enter numbers of same cards appearing thrice or three cards which are different.");
						throw new Exception();
					}
					player.exchangeCards(player.getPlayerCardExchangeCount(), player.getSameCardExchangeThrice(),
							player, cardNumbers);
					player.setExchanged(false);
					int count = GameMap.getCardExchangeCountinTheGame();
					armiesInExchange = (count + 1) * 5;
					GameMap.cardExchangeCountinTheGame = GameMap.getCardExchangeCountinTheGame() + 1;

				}
			}
		}

		return 0;

	}

}
