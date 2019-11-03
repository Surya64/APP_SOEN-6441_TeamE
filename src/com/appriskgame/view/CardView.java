package com.appriskgame.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.controller.Player;
import com.appriskgame.model.Card;
import com.appriskgame.model.GameMap;

/**
 * This Class is to check the player has enough cards to exchange and for
 * observer pattern
 *
 * @author Shruthi
 *
 */

public class CardView {

	public int exchangeCards(Player player) throws Exception {

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
					if (player.getNumOfTypeCardsExchanged() < 3 && (player.getSameCardExchangeThrice() == null
							|| player.getSameCardExchangeThrice().isEmpty())) {
						System.out.println(
								"Please enter numbers of same cards appearing thrice or three cards which are different.");
						throw new Exception();
					}
//					player.exchangeCards(player.getNumOfTypeCardsExchanged(), player.getSameCardExchangeThrice(),
//							player, cardNumbers);
					player.setExchanged(false);
					int count = GameMap.getCardExchangeCountinTheGame();
					armiesInExchange = (count + 1) * 5;
					GameMap.cardExchangeCountinTheGame = GameMap.getCardExchangeCountinTheGame() + 1;

//					player = player.exChangeCardTerritoryExist(exchangeCards, player);

					System.out.println("Player recieves " + armiesInExchange + " armies for exchanging the cards");
					player.setPlayerCardExchangeCount(player.getPlayerCardExchangeCount() + 1);
				} else {
					System.out.println("Not Exchanging the cards to armies");
				}

			} else {
				int i = 0;
				for (Card card : player.getCardList()) {
					System.out.println(card.getType() + ", " + card.getCountry().getCountryName() + " : " + (++i));
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
				if (player.getNumOfTypeCardsExchanged() < 3 && (player.getSameCardExchangeThrice() == null
						|| player.getSameCardExchangeThrice().isEmpty())) {
					System.out.println(
							"Please enter numbers of same cards appearing thrice or three cards which are different.");
					throw new Exception();
				}
//				player.exchangeCards(player.getNumOfTypeCardsExchanged(), player.getSameCardExchangeThrice(), player,
//						cardNumbers);
				player.setExchanged(false);
				int count = GameMap.getCardExchangeCountinTheGame();
				armiesInExchange = (count + 1) * 5;
				GameMap.cardExchangeCountinTheGame = GameMap.getCardExchangeCountinTheGame() + 1;
//				player = player.exChangeCardTerritoryExist(exchangeCards, player);

				System.out.println("Player recieves " + armiesInExchange + " armies for exchanging the cards");
				player.setPlayerCardExchangeCount(player.getPlayerCardExchangeCount() + 1);

			}
		} else if (!cardExchangePossible) {
			System.out.println("Not enough cards to exchange , moving to reinforcement");
		}

		return armiesInExchange;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
//	@Override
////	public void update(Observable arg0, Object arg1) {
////		GamePlayer player = (GamePlayer) arg0;
////		if (player != null && Player.reinforcePhase.equals(player.getCurrentPhase()) && !(player.getCardList() == null)
////				&& player.getExchanged()) {
////
////			System.out.println("**************In Card View*************** ");
////			List<Card> cardList = player.getCardList();
////			System.out.println("Cards after exchanging the cards");
////
////			for (Card card : cardList) {
////				System.out.println(card.getName());
////			}
////
////		}
////
////	}
}
