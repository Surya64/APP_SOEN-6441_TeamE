package com.appriskgame.controller;

import java.io.IOException;

import com.appriskgame.model.GameMap;

public interface ReadAndWrite {
	public GameMap readGameMap(String inputGameMapName) throws IOException;

	public void writeGameMap(String ouputGameMapName, String mapFileName, GameMap gameMap) throws IOException;
}