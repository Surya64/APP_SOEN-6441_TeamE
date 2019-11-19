package com.appriskgame.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.appriskgame.model.GameMap;

public class Tournament {

	List<String> maps;
	List<String> playersStrategies;
	int noOfGames;
	int turns;
	MapOperations loadGameMap;
	String[][] results;

	ArrayList<String> preDefinedStrategies = new ArrayList<String>() {
		{
			add("Aggressive");
			add("Benevolent");
			add("Random");
			add("Cheater");
		}
	};

	public static void main(String[] args) throws Exception {

		System.out.println("Enter the command?");

		Scanner sc = new Scanner(System.in);
		Tournament tournament = new Tournament();
		String tournamentDetails = sc.nextLine();

		String[] tournamentList = tournamentDetails.split(" ");

		if (tournament.validateCommand(tournamentList)) {
			tournament.fillTournamentData(tournamentList[2], tournamentList[4], tournamentList[6], tournamentList[8]);
			if(!tournament.mapsValidation(tournament.maps))
			{
				System.out.println("Maps should be Different");
			}
			else {
			
				tournament.results = new String[tournament.maps.size()][tournament.noOfGames];

				for (int i = 0; i < tournament.maps.size(); i++) {
					for (int j = 0; j < tournament.noOfGames; j++) {
						tournament.results[i][j] = tournament.startGame(j, tournament.maps.get(i),
								tournament.playersStrategies,tournament.turns);
//						System.out.println("SAS");
					}
				}

				tournament.generateReport();
			}

			
		}

	}

	@SuppressWarnings("finally")
	public boolean validateCommand(String[] tournamentList) {

		boolean decision = false;
		try {
			String tournament = tournamentList[0];
			String maps = tournamentList[1];
			String stategies = tournamentList[3];
			String game = tournamentList[5];
			String turn = tournamentList[7];
			int games = Integer.parseInt(tournamentList[6]);
			int turns = Integer.parseInt(tournamentList[8]);

			if (tournament.equalsIgnoreCase("tournament") && game.equalsIgnoreCase("-G") && turn.equalsIgnoreCase("-D")
					&& maps.equalsIgnoreCase("-M") && stategies.equalsIgnoreCase("-P")) {

				if (games >= 1 && games <= 5) {

					if (turns >= 10 && turns <= 50) {
						decision = true;
						return decision;
					} else {
						System.out.println("Invalid Turns [10 to 50]");
						decision = false;
						return decision;

					}

				} else {
					System.out.println("Invalid Number of Games [1 to 5]");
					decision = false;
					return decision;
				}
			} else {
				System.out.println(
						"Invalid Command [Correct Syntax:tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D maxnumberofturns]");
				decision = false;
				return decision;
			}
		} catch (Exception ex) {
			System.out.println(
					"Invalid Command [Correct Syntax:tournament -M listofmapfiles[1 to 5 different maps] -P listofplayerstrategies -G numberofgames -D maxnumberofturns  ]");
			decision = false;
		} finally {
			return decision;
		}

	}

	public void generateReport() {
//		System.out.print("|                    |");
		System.out.print("                      ");

		printHeaders();
//		System.out.println();
		printReport();
	}

	public void printHeaders() {
		for (int i = 0; i < this.noOfGames; i++) {

			System.out.print(getFormattedGameNumber(i + 1));
		}

	}

	public void printReport() {
		System.out.println();
		for (int i = 0; i < this.maps.size(); i++) {

			System.out.print(getFormattedMapName(this.maps.get(i)));
			for (int j = 0; j < this.noOfGames; j++) {

				System.out.print(getFormattedStrategry(this.results[i][j]));

			}
			System.out.println();
		}
	}

	public String getFormattedStrategry(String strategy) {

		int currentLength = strategy.length();

		String formattedStrategy = " " + strategy;
		for (int i = currentLength; i < 18; i++) {
			formattedStrategy = formattedStrategy + " ";
			if (i == 17) {
				formattedStrategy = formattedStrategy + " ";
			}

		}
		return formattedStrategy;

	}

	public String getFormattedMapName(String mapName) {

		int currentLength = mapName.length();

		String formattedMapName = " " + mapName;
		for (int i = currentLength; i < 20; i++) {
			formattedMapName = formattedMapName + " ";
			if (i == 19) {
				formattedMapName = formattedMapName + " ";
			}
		}
		return formattedMapName;

	}

	public String getFormattedGameNumber(int number) {

//		String formattedGame="|" + "Game " + number + "            |";
		String formattedGame = " " + "Game " + number + "             ";
		return formattedGame;

	}

	public String startGame(int gameNumber, String mapName, List<String> playerStrategies,int turns) throws Exception {

		String workingDir = System.getProperty("user.dir");
		String mapLocation = workingDir + "/resources/maps/";
		GameMap createMapGraph = new GameMap();
		String inputGameMapName = mapLocation + mapName + ".map";
		loadGameMap = new MapOperations();
		if (loadGameMap.isMapExists(mapName)) {
			Player start = new Player();
			createMapGraph = loadGameMap.readGameMapTournament(inputGameMapName);
			if (!createMapGraph.getContinents().isEmpty()) {

				for (int i = 0; i < playerStrategies.size(); i++) {
					if (preDefinedStrategies.contains(playerStrategies.get(i))) {

					} else {
						return "Invalid Strategy";
					}
				}
//					start.gamePlay(createMapGraph);
				return getGameResult(gameNumber, mapName, playerStrategies,turns);
			} else {
				return "Incorrect Map";
			}
		} else {
			return "File Not Found";
		}

	}

	public String getGameResult(int gameNumber, String mapName, List<String> playerStrategies,int turns) {
		String strat = "";

		Random random = new Random();
		int i = random.nextInt(4) + 0;
		return playerStrategies.get(i);
	}

	
	
	
	public boolean mapsValidation(List<String> maps)
	{
		List<String> uniqueMaps=new ArrayList<String>();
		
		for(int i=0;i<maps.size();i++)
		{
			
			if(uniqueMaps.contains(maps.get(i).toString()))
			{
				return false;
			}
			uniqueMaps.add(maps.get(i).toString());
		}
		return true;
	}
	
	
	
	
	
	
	public void fillTournamentData(String maps, String Strategies, String numberOfGames, String turns)

	{
		this.maps = getMaps(maps);
		this.playersStrategies = getPlayersStrategies(Strategies);
		this.noOfGames = getnoOfGames(numberOfGames);
		this.turns = getTurns(turns);

	}

	public int getTurns(String turns) {
		return Integer.parseInt(turns.trim());

	}

	public int getnoOfGames(String noOfGames) {
		return Integer.parseInt(noOfGames.trim());

	}

	public List<String> getPlayersStrategies(String playersStrategy) {
		String[] playersStrategyList = playersStrategy.split(",");
		List<String> listOfStrategies = new ArrayList<String>();
		for (int i = 0; i < playersStrategyList.length; i++) {
			listOfStrategies.add(playersStrategyList[i]);
		}

		return listOfStrategies;
	}

	public List<String> getMaps(String maps) {
		String[] mapsList = maps.split(",");
		List<String> listOfMaps = new ArrayList<String>();
		for (int i = 0; i < mapsList.length; i++) {
			listOfMaps.add(mapsList[i]);
		}

		return listOfMaps;
	}

}
