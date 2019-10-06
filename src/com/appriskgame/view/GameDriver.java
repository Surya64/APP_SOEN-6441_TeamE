package com.appriskgame.view;

import java.io.IOException;

import com.appriskgame.model.GameMap;
import com.appriskgame.services.MapOperations;
import com.appriskgame.services.MapValidation;
import com.appriskgame.services.StartupPhase;


public class GameDriver {

	public static void main(String[] args) throws Exception {
		boolean uploadSuccessful = false;
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
		MapValidation validate = new MapValidation();
		try {
			uploadSuccessful = validate.validateMap(ouputMapName);
			if (uploadSuccessful) {
					System.out.println("Thank You !!");
					GameMap gameMap = new GameMap();
					gameMap = validate.getMapGraph();
					StartupPhase start = new StartupPhase();
					start.gamePlay(gameMap);
			} else {
				System.out.println(MapValidation.getError());
				System.out.println(
						"\nPlease rectify all the above mentioned issues and upload the file again");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
