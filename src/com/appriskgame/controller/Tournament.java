package com.appriskgame.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.appriskgame.model.GameMap;

public class Tournament {

	ArrayList<String> maps;
	ArrayList<String> playersStrategies;
	int noOfGames;
	int turns;
	MapOperations loadGameMap;
	String[][] results;
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	ArrayList<String> preDefinedStrategies = new ArrayList<String>() {
		{
			add("aggressive-aggressive");
			add("benevolent-benevolent");
			add("random-random");
			add("cheater-cheater");
		}
	};

	public static void startTournament() throws Exception {

		System.out.println("Enter the Tournament command?");
		Tournament tournament = new Tournament();
		String tournamentDetails = br.readLine().trim();

		String[] tournamentList = tournamentDetails.split(" ");
		if (tournament.validateCommand(tournamentList)) {
			tournament.fillTournamentData(tournamentList[2], tournamentList[4], tournamentList[6], tournamentList[8]);
			if (!tournament.mapsValidation(tournament.maps)) {
				System.out.println("Maps should be Different");
			} else {
				tournament.results = new String[tournament.maps.size()][tournament.noOfGames];
				for (int i = 0; i < tournament.maps.size(); i++) {
					for (int j = 0; j < tournament.noOfGames; j++) {
						tournament.results[i][j] = tournament.startGame(j, tournament.maps.get(i),
								tournament.playersStrategies, tournament.turns);
					}
				}
				tournament.generateReport();
				System.exit(0);
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
		System.out.print("                      ");
		printHeaders();
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
		String formattedGame = " " + "Game " + number + "             ";
		return formattedGame;
	}

	public String startGame(int gameNumber, String mapName, ArrayList<String> playerStrategies, int turns)
			throws Exception {
		String workingDir = System.getProperty("user.dir");
		String mapLocation = workingDir + "/resources/maps/";
		GameMap createMapGraph = new GameMap();
		String inputGameMapName = mapLocation + mapName + ".map";
		loadGameMap = new MapOperations();
		if (loadGameMap.isMapExists(mapName)) {
			createMapGraph = loadGameMap.readGameMap(inputGameMapName);
			if (!createMapGraph.getContinents().isEmpty()) {
				for (int i = 0; i < playerStrategies.size(); i++) {
					if (preDefinedStrategies.contains(playerStrategies.get(i))) {
					} else {
						return "Invalid Strategy";
					}
				}
				return getGameResult(gameNumber, inputGameMapName, playerStrategies, turns);
			} else {
				return "Incorrect Map";
			}
		} else {
			return "File Not Found";
		}
	}

	public String getGameResult(int gameNumber, String mapName, ArrayList<String> playerStrategies, int turns)
			throws Exception {
		Player p = new Player();
		MapOperations mp = new MapOperations();
		GameMap gm = mp.readGameMap(mapName);
		gm.setMode("tournament");
		String result = p.gamePlayTournament(gm, playerStrategies, turns);
		String[] formattedResult = result.split("-");
		return formattedResult[1];
	}

	public boolean mapsValidation(List<String> maps) {
		List<String> uniqueMaps = new ArrayList<String>();
		for (int i = 0; i < maps.size(); i++) {
			if (uniqueMaps.contains(maps.get(i).toString())) {
				return false;
			}
			uniqueMaps.add(maps.get(i).toString());
		}
		return true;
	}

	public void fillTournamentData(String maps, String Strategies, String numberOfGames, String turns) {
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

	public ArrayList<String> getPlayersStrategies(String playersStrategy) {
		String[] playersStrategyList = playersStrategy.split(",");
		ArrayList<String> listOfStrategies = new ArrayList<String>();
		for (int i = 0; i < playersStrategyList.length; i++) {
			listOfStrategies.add(playersStrategyList[i] + "-" + playersStrategyList[i]);
		}
		return listOfStrategies;
	}

	public ArrayList<String> getMaps(String maps) {
		String[] mapsList = maps.split(",");
		ArrayList<String> listOfMaps = new ArrayList<String>();
		for (int i = 0; i < mapsList.length; i++) {
			listOfMaps.add(mapsList[i]);
		}
		return listOfMaps;
	}

}
