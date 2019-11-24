package com.appriskgame.controller;

import java.io.IOException;

import com.appriskgame.model.GameMap;

public class MapAdapter implements MapReadAndWrite {

	ReadAndWrite readAndWrite;

	public MapAdapter(String format) {
		if (format.equalsIgnoreCase("Domination")) {
			readAndWrite = new ReadAndWriteDomination();

		} else if (format.equalsIgnoreCase("Conquest")) {
			readAndWrite = new ReadAndWriteMapConquest();
		}

	}

	@Override
	public GameMap readGameMap(String inputGameMapName, String format) throws IOException {

		if (format.equalsIgnoreCase("Domination")) {
			return readAndWrite.readGameMap(inputGameMapName);

		} else if (format.equalsIgnoreCase("Conquest")) {
			return readAndWrite.readGameMap(inputGameMapName);
		}
		return null;
	}

	@Override
	public void writeGameMap(String ouputGameMapName, String mapFileName, GameMap gameMap, String format) {

		if (format.equalsIgnoreCase("Domination")) {
			readAndWrite.writeGameMap(ouputGameMapName, mapFileName, gameMap);

		} else if (format.equalsIgnoreCase("Conquest")) {
			readAndWrite.writeGameMap(ouputGameMapName, mapFileName, gameMap);
		}
	}

}