package com.appriskgame.services;

import java.util.ArrayList;
import java.util.Iterator;

import com.appriskgame.model.GamePlayer;

public class RoundRobinAllocator {
	private ArrayList<GamePlayer> listOfPlayers;
	private Iterator<GamePlayer> iterator;

	public RoundRobinAllocator(ArrayList<GamePlayer> playersList) {
		this.listOfPlayers = playersList;
		iterator = playersList.iterator();
	}

	public GamePlayer nextTurn() {
		if (!iterator.hasNext()) {
			iterator = listOfPlayers.iterator();
		}
		return iterator.next();
	}
}
