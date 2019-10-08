package com.appriskgame.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.model.GameMap;
import com.appriskgame.services.MapOperations;

public class GameDriver {
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public static void setUp() throws Exception {
		MapOperations loadGameMap;
		GameMap gameMap;
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
				break;
			case 2:
				loadGameMap = new MapOperations();
				gameMap = loadGameMap.loadFile();
				if(gameMap.getContinents() == null) {
					System.out.println("Thank You!!");
				}
				else {
					
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

	public static void main(String[] args) throws Exception {
		setUp();
	}

}
