package com.appriskgame.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.model.GameMap;
import com.appriskgame.services.MapOperations;
import com.appriskgame.services.StartupPhase;

public class GameDriver {
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static MapOperations loadGameMap;
	private static GameMap gameMap;
	static String workingDir = System.getProperty("user.dir");
	static String mapLocation = workingDir + "/resources/maps/";

	public static void setUp() throws Exception {

		boolean exit = true;

		while (exit) {
			exit = false;
			System.out.println("\nWelcome to Risk Game!");
			System.out.println("\nChoose the below options\n");
			System.out.println("1. Create a Map.");
			System.out.println("2. Load an existing map");
			System.out.println("3. Exit");
			System.out.println("\nPlease enter your choice below:");
			Pattern pattern = Pattern.compile("[0-9]+");
			String option = br.readLine().trim();
			Matcher match = pattern.matcher(option.trim());
			while (!(match.matches())) {
				System.out.println("Please enter a valid option(number) from the game menu!");
				option = br.readLine().trim();
				match = pattern.matcher(option.trim());
			}
			switch (Integer.parseInt(option)) {
			case 1:
				loadGameMap = new MapOperations();
				gameMap = loadGameMap.createFile();
				startGame();

				break;
			case 2:
				loadGameMap = new MapOperations();
				gameMap = loadGameMap.loadFile();
				if (gameMap.getContinents() == null) {
					System.out.println("Thank You!!");
				} else {
					startGame();
				}
				break;
			case 3:
				exit = false;
				System.out.println("Thank You!!");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid option. Please choose the correct option.");

			}
		}

	}

	private static void startGame() throws Exception {
		System.out.println("Do you want to start the game? (Yes or No)");
		try {
			GameMap createMapGraph = new GameMap();
			String choice = br.readLine().trim();
			while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No") || choice == null)) {
				System.err.println("\nPlease enter the choice as either Yes or No:");
				System.out.flush();
				choice = br.readLine().trim();
			}

			if (choice.equalsIgnoreCase("Yes")) {
				System.out.println("Please enter the Load File Command");
				String command = br.readLine().trim();
				String[] cmdDetails = command.split(" ");
				String cmdType = cmdDetails[0];
				String inputGameMapName = mapLocation + cmdDetails[1] + ".map";
				if (cmdType.equals("loadmap")) {
					StartupPhase start = new StartupPhase();
					loadGameMap = new MapOperations();
					createMapGraph = loadGameMap.readGameMap(inputGameMapName);
					start.gamePlay(createMapGraph);
				}
			} else {
				System.out.println("\nThank you!");
				System.exit(0);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		setUp();
	}

}
