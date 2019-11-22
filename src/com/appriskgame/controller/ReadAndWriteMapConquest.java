package com.appriskgame.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;

public class ReadAndWriteMapConquest  implements ReadAndWrite{

	
//	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	String workingDir = System.getProperty("user.dir");
	String mapLocation = workingDir + "/resources/maps/";
	public GameMap gameMap = new GameMap();
	static int connectableCountries = 0;
	/**
	 * This Method is to load the contents of Continents, Countries and neighboring
	 * countries.
	 *
	 * @param inputGameMapName FileName to read the Map.
	 * @return Game Map Object.
	 * @throws IOException FileNotFound Exception.
	 */

	public GameMap readGameMap(String inputGameMapName) throws IOException {
		HashMap<String, Country> countrySet = new HashMap<>();
		MapValidation validate = new MapValidation();
		gameMap = new GameMap();
		connectableCountries = 0;
		String GameMapName = inputGameMapName;
		boolean uploadSuccessful = false;
		String data = "";
		uploadSuccessful = validate.validateMapDomination(GameMapName);
//		uploadSuccessful = true;
		String[] requiredData=null;
		if (uploadSuccessful) {
			try {
				data = new String(Files.readAllBytes(Paths.get(GameMapName)));
				requiredData= data.split("\\r\\n\\r\\n");
//				data = requiredData[1];
			} catch (IOException e) {
				e.printStackTrace();
			}
			String[] formattedData = requiredData;
			fillContinentsInGameMap(formattedData[1]);
			fillCountriesInGameMap(formattedData[2]);
			fillNeighboringCountriesInGameMap(formattedData[2]);
			gameMap.getCountries().forEach(country -> {
				countrySet.put(country.getCountryName(), country);
				gameMap.setCountrySet(countrySet);
			});
			boolean res = isConnected(gameMap);
//			boolean res = true;
			if (res == true) {
				return gameMap;
			} else {
				System.out.println("Graph Not connected");
				gameMap = null;
				return new GameMap();
			}
		} else {
			System.out.println(MapValidation.getError());
			System.out.println("\nPlease rectify all the above mentioned issues.");
		}

		return new GameMap();
	}
	
	
	
	/**
	 * This Method is to write the Continents, Countries and boundaries details to
	 * the map file.
	 *
	 * @param ouputGameMapName full path of the file to write.
	 * @param mapFileName      File Name of the writing file.
	 * @throws IOException IO
	 */

	public void writeGameMap(String ouputGameMapName, String mapFileName,GameMap gameMap ) throws IOException {
		this.gameMap=gameMap;
		File GameMapName = new File(ouputGameMapName);
		FileWriter fw = new FileWriter(GameMapName);
		BufferedWriter bw = new BufferedWriter(fw);
		String fileData = getFileTags(mapFileName);
		try {
			bw.write(fileData);
			bw.write("\r\n\r\n");
			String continentsData = getContinents();
			bw.write(continentsData);
			bw.write("\r\n\r\n");
			String countriesData = getContriesAndNeighbour();
			bw.write(countriesData);
//			bw.write("\r\n\r\n");
//			String boundariesData = getBoundaries();
//			bw.write(boundariesData);
			bw.close();
		} catch (IOException e) {
			System.err.println("File not found exception");
		}
	}
	
	
	/**
	 * This method is to fill the Continents Details in the Game Map.
	 *
	 * @param ContinentsString String of all Continent details.
	 */

	public void fillContinentsInGameMap(String ContinentsString) {
		String[] continentList = ContinentsString.split("\\r\\n");
		for (int i = 1; i < continentList.length; i++) {
			String[] ContinentDetails = continentList[i].split("=");
			Continent continent = new Continent();
			continent.setContinentName(ContinentDetails[0]);
			continent.setContinentControlValue(Integer.parseInt(ContinentDetails[1]));
			gameMap.getContinents().add(continent);
		}
	}
	
	/**
	 * This method is to fill the Countries Details in the Game Map.
	 *
	 * @param CountriesString String of all Countries details.
	 */

	public void fillCountriesInGameMap(String CountriesString) {
		String[] countriesList = CountriesString.split("\\r\\n");

		for (int i = 1; i < countriesList.length; i++) {
			String[] countryDetails = countriesList[i].split(",");
//			String continentName = getContinentName(Integer.parseInt(countryDetails[2]));
			String continentName = countryDetails[3];
//			int continentNumber = Integer.parseInt(countryDetails[2]);
//			Continent continent = gameMap.getContinents().get(continentNumber - 1);
			Continent continent = getContinentObject(continentName);
			Country country = new Country();
			country.setCountryName(countryDetails[0]);
			country.setContinentName(continentName);
			country.setPartOfContinent(continent);
			continent.getListOfCountries().add(country);
			gameMap.getCountries().add(country);
		}
	}
	
	
	/**
	 * This method is to fill the Neighboring Countries Details in the Game Map.
	 *
	 * @param neighboringCountriesString String of all Neighboring Countries
	 *                                   details.
	 */

	public void fillNeighboringCountriesInGameMap(String neighboringCountriesString) {
		String[] neighbouringCountriesList = neighboringCountriesString.split("\\r\\n");

		for (int i = 1; i < neighbouringCountriesList.length; i++) {
			String[] arrayOfCountries = neighbouringCountriesList[i].split(",");
//			int currentCountryNumber = Integer.parseInt(arrayOfCountries[0]);
			for (int j = 4; j < arrayOfCountries.length; j++) {
//				int neighbourCountryNumber = Integer.parseInt(arrayOfCountries[j]) - 1;
//				Country neighbourCountry = gameMap.getCountries().get(neighbourCountryNumber);
				Country neighbourCountry = getCountryObject(arrayOfCountries[j]);
//				Country currentCountry = gameMap.getCountries().get(currentCountryNumber - 1);
				Country currentCountry = getCountryObject(arrayOfCountries[0]);
				currentCountry.getNeighbourCountriesToAdd().add(neighbourCountry);
				currentCountry.getNeighbourCountries().add(neighbourCountry.getCountryName());
			}
		}
	}
	
	public Continent getContinentObject(String countryName)
	{
		
		int desiredIndex=0;
		
		for(int i=0;i<gameMap.getContinents().size();i++)
		{
		 if(gameMap.getContinents().get(i).getContinentName().equalsIgnoreCase(countryName))
		 {
			 
			 desiredIndex=i;
			 break;
		 }
		}
		
		return gameMap.getContinents().get(desiredIndex);
	}
	
	
	public Country getCountryObject(String countryName)
	{
		
		int desiredIndex=0;
		
		for(int i=0;i<gameMap.getCountries().size();i++)
		{
		 if(gameMap.getCountries().get(i).getCountryName().equalsIgnoreCase(countryName))
		 {
			 
			 desiredIndex=i;
			 break;
		 }
		}
		
		return gameMap.getCountries().get(desiredIndex);
	}
	
	
	
	/**
	 * This Method give standard file tags to the output file.
	 *
	 * @param ouputGameMapName output File Name.
	 * @return String of file Tags.
	 */

	public String getFileTags(String ouputGameMapName) {
		
		String map="[Map]\r\n";
		String author="author=Team 7\r\n";
		String image="image="+ouputGameMapName+".bmp\r\n";
		String wrap="wrap=yes\r\n";
		String scroll="scroll=none\r\n";
		String warn="warn=yes";
		String fullFormat = map + author + image+wrap+scroll+warn;
		return fullFormat;
	}
	

	/**
	 * This Method will give the continent details in a standard format.
	 *
	 * @return String consists of continent details in a standard format.
	 *
	 */

	public String getContinents() {
		String continentsDetails = "[continents]";
		for (int i = 0; i < gameMap.getContinents().size(); i++) {
			Continent continent = gameMap.getContinents().get(i);
			String continentDetails = continent.getContinentName() + "=" + continent.getContinentControlValue();
//					+ "#99NoColor";
			continentsDetails = continentsDetails + "\r\n" + continentDetails;
		}
		return continentsDetails;
	}
	
	/**
	 * This Method will give the continent details in a standard format.
	 *
	 * @return String consists of continent details in a standard format.
	 *
	 */

	public String getContriesAndNeighbour() {
		String countriesDetails = "[countries]\r\n";
		for (int i = 0; i < gameMap.getCountries().size(); i++) {
			String currentLine="";
			Country country = gameMap.getCountries().get(i);
			currentLine=country.getCountryName().toString();
			currentLine=currentLine+","+"100"+","+"100";
			currentLine=currentLine+","+country.getContinentName();
			String neigbourDetails="";
			for(int j=0;j<country.getNeighbourCountries().size();j++)
			{
				String currentNeighbourDetail=country.getNeighbourCountries().get(j).toString();
				neigbourDetails=neigbourDetails+","+currentNeighbourDetail;
			}
			
			currentLine=currentLine+neigbourDetails+"\r\n";
			countriesDetails=countriesDetails+currentLine;
		}
		return countriesDetails;
	}
	//////////////////////////////////////////
	
	

	/**
	 * This method checks for continent connectivity.
	 * 
	 * @param gameMap It is game Map object
	 * @return True if it is connected false if not connected.
	 */
	public boolean isConnected(GameMap gameMap) {

		for (int j = 0; j < gameMap.getContinents().size(); j++) {
			ArrayList<Country> listOfCountries = gameMap.getContinents().get(j).getListOfCountries();
			int[] visited = new int[listOfCountries.size()];
			for (int i = 0; i < listOfCountries.size(); i++) {
				depthFirstSearch(visited, i, listOfCountries);
				visited = new int[listOfCountries.size()];
				if (connectableCountries != listOfCountries.size()) {
					return false;
				} else {

				}
				connectableCountries = 0;
			}
		}

		return true;
	}

	/**
	 * This method is used to depth first search of a graph.
	 * 
	 * @param visited         Node visited value
	 * @param currentindex    Current index value
	 * @param listOfCountries List of countries.
	 */
	public void depthFirstSearch(int[] visited, int currentindex, ArrayList<Country> listOfCountries) {

		if (visited[currentindex] == 1) {
			return;
		}
		visited[currentindex] = 1;

		ArrayList<Integer> list = getNeighbourCountriesIntegerList(
				listOfCountries.get(currentindex).getNeighbourCountriesToAdd(), listOfCountries);

		for (int vert : list) {
			depthFirstSearch(visited, vert, listOfCountries);
		}
		connectableCountries = connectableCountries + 1;

	}
	
	/**
	 * This method gives neighbor countries' list in integer.
	 * 
	 * @param neighbourCountries List of neighbor countries.
	 * @param listOfCountries    List of countries.
	 * @return List of neighbor countries.
	 */
	public ArrayList<Integer> getNeighbourCountriesIntegerList(List<Country> neighbourCountries,
			ArrayList<Country> listOfCountries) {
		ArrayList<Integer> neighbourCountriesIntegerList = new ArrayList<Integer>();

		for (Integer i = 0; i < neighbourCountries.size(); i++) {
			String countryName = neighbourCountries.get(i).getCountryName();

			for (int j = 0; j < listOfCountries.size(); j++) {
				String checkName = listOfCountries.get(j).getCountryName();

				if (countryName.equalsIgnoreCase(checkName)) {
					neighbourCountriesIntegerList.add(j);
				}
			}
		}
		return neighbourCountriesIntegerList;
	}
	
}
