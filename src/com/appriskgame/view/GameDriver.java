package com.appriskgame.view;

import java.io.IOException;

import com.appriskgame.services.MapOperations;

public class GameDriver {

	public static void main(String[] args) {
		String workingDir = System.getProperty("user.dir");
		String inputMapName = workingDir + "/resources/maps/" + "ameroki.map";
		String ouputMapName = workingDir + "/resources/maps/" + "out.map";
		MapOperations loadMap = new MapOperations();
		loadMap.readMap(inputMapName);
		try {
			loadMap.writeMap(ouputMapName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!loadMap.connectivity()) {
			System.out.println("Map is not a connected graph.");
		}

	}

}
