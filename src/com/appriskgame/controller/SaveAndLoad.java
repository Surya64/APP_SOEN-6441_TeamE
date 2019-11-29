package com.appriskgame.controller;

import java.io.IOException;

import com.appriskgame.model.GameMap;

public interface SaveAndLoad {

	public void saveGame(GameMap gameMap) throws IOException;
	public void readGame() throws Exception ;
}
