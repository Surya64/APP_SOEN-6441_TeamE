package com.appriskgame.view;

import java.io.IOException;

import com.appriskgame.services.MapOperations;
import com.appriskgame.services.MapValidation;

public class GameDriver {

	public static void main(String[] args) {
		boolean uploadSuccessful = false;
		String workingDir = System.getProperty("user.dir");
		String inputMapName = workingDir + "/resources/maps/" + "ameroki.map";
		String ouputMapName = workingDir + "/resources/maps/" + "out.map";
		MapOperations loadMap = new MapOperations();
		loadMap.readGameMap(inputMapName);
		try {
			loadMap.writeGameMap(ouputMapName, "out");
		} catch (IOException e) {
			e.printStackTrace();
		}
		MapValidation validate = new MapValidation();
		try {
			uploadSuccessful = validate.validateMap(ouputMapName);
			if (uploadSuccessful) {
				System.out.println("Thank You !!");
				System.exit(0);
			} else {
				System.out.println(MapValidation.getError());
				System.out.println("\nPlease rectify all the above mentioned issues and upload the file again");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
