package com.appriskgame.services;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.Map;

public class MapOperations {

	Map map = new Map();

	public static void main(String[] args) throws IOException {

		MapOperations loadMap = new MapOperations();
//		For Read and write the Map
		String inputMapName = "C:\\Users\\saich\\Desktop\\Test\\ameroki.map";
		String ouputMapName = "C:\\Users\\saich\\Desktop\\Result\\out.map";
		loadMap.readMap(inputMapName);

		Scanner sc = new Scanner(System.in);
		System.out.println("Do you want to continue?");

		int ops = sc.nextInt();
		while (ops == 1) {
			System.out.println("Enter the command");
			sc.nextLine();
			String command = sc.nextLine();
			String[] cmdDetails = command.split(" ");
			String cmdType = cmdDetails[0];
			String opsType = cmdDetails[1];

			if (cmdType.equals("editcontinent")) {

				if (opsType.equals("-add")) {
					loadMap.addContinentToMap(cmdDetails[2], Integer.parseInt(cmdDetails[3]));
				} else if (opsType.equals("-remove")) {

					if (loadMap.doesContinentExit(cmdDetails[2])) {
						loadMap.removeContinentFromMap(cmdDetails[2]);
					} else {
						System.out.println(cmdDetails[2] + "  " + "Continent is not present in the map");
					}

				}

			} else if (cmdType.equals("editcountry")) {
				if (opsType.equals("-add")) {

					if (loadMap.doesContinentExit(cmdDetails[3])) {
						loadMap.addCountryToMap(cmdDetails[2], cmdDetails[3]);
					} else {
						System.out.println(cmdDetails[3] + "Continent is not present in the map");
					}

				} else if (opsType.equals("-remove")) {
					if (cmdDetails.length == 3) {

						if (loadMap.doesCountryExit(cmdDetails[2])) {
							loadMap.removeCountryFromMap(cmdDetails[2]);
						} else {
							System.out.println(cmdDetails[2] + "  " + "Country is not present in the map");
						}

					}

				}

			} else if (cmdType.equals("editneighbor")) {

				if (opsType.equals("-add")) {
					if (loadMap.doesCountryExit(cmdDetails[2])) {
						if (loadMap.doesCountryExit(cmdDetails[3])) {
							loadMap.addNeighborCountryToMap(cmdDetails[2], cmdDetails[3]);
							loadMap.addNeighborCountryToMap(cmdDetails[3], cmdDetails[2]);
						} else {
							System.out.println(cmdDetails[3] + "  " + "Country is not present in the map");
						}
					} else {
						System.out.println(cmdDetails[2] + "  " + "Country is not present in the map");
					}

				} else if (opsType.equals("-remove")) {

					if (loadMap.doesCountryExit(cmdDetails[2])) {
						if (loadMap.doesCountryExit(cmdDetails[3])) {
							loadMap.removeNeighborCountryFromMap(cmdDetails[2], cmdDetails[3]);
							// Remove the same in other way
							loadMap.removeNeighborCountryFromMap(cmdDetails[3], cmdDetails[2]);
						} else {
							System.out.println(cmdDetails[3] + "  " + "Country is not present in the map");
						}
					} else {
						System.out.println(cmdDetails[2] + "  " + "Country is not present in the map");
					}

				}
			}

			System.out.println("Enter 1 to continue");
			ops = sc.nextInt();
		}

		// Write to File
		loadMap.writeMap(ouputMapName);
		System.out.println();

	}

	public void readMap(String inputMapName) {
		String data = "";
		String mapName = inputMapName;
		try {
			data = new String(Files.readAllBytes(Paths.get(mapName)));
		} catch (IOException e) {

			e.printStackTrace();
		}
		String[] formattedData = data.split("\\r\\n\\r\\n");

		fillContinentsInMap(formattedData[3]);
		fillCountriesInMap(formattedData[4]);
		fillNeighboringCountriesInMap(formattedData[5]);

	}

	public void fillContinentsInMap(String ContinentsString) {
		String[] continentList = ContinentsString.split("\\r\\n");

		for (int i = 1; i < continentList.length; i++) {
			String[] ContinentDetails = continentList[i].split(" ");
			Continent continent = new Continent();
			continent.setContinentName(ContinentDetails[0]);
			continent.setContinentControlValue(Integer.parseInt(ContinentDetails[1]));
			map.getContinents().add(continent);
		}
	}

	public int getContinentNumber(String continentName) {
		int cotinenentNumber = 0;

		for (int i = 0; i < map.getContinents().size()
				&& !map.getContinents().get(i).getContinentName().equals(continentName); i++) {
			cotinenentNumber = i + 1;
		}
		int actualContinentNumber = cotinenentNumber + 1;

		return actualContinentNumber;
	}

	public String getContinentName(int continentNumber) {
		String cotinenentName = "";

		cotinenentName = map.getContinents().get(continentNumber - 1).getContinentName();

		return cotinenentName;
	}

	public int getCountryNumber(String countryName) {
		int countryNumber = 0;

		for (int i = 0; i < map.getCountries().size()
				&& !map.getCountries().get(i).getCountryName().equals(countryName); i++) {
			countryNumber = i + 1;
		}
		int actualCountryNumber = countryNumber + 1;

		return actualCountryNumber;
	}

	public String getCountryName(int countryNumber) {
		String countryName = "";

		countryName = map.getCountries().get(countryNumber).getCountryName();

		return countryName;
	}

	public void fillCountriesInMap(String CountriesString) {
		String[] countriesList = CountriesString.split("\\r\\n");

		for (int i = 1; i < countriesList.length; i++) {
			String[] countryDetails = countriesList[i].split(" ");
			String continentName = getContinentName(Integer.parseInt(countryDetails[2]));
			int continentNumber = Integer.parseInt(countryDetails[2]);
			Continent continent = map.getContinents().get(continentNumber - 1);
			Country country = new Country();
			country.setCountryName(countryDetails[1]);
			country.setContinentName(continentName);
			continent.getListOfCountries().add(country);

			map.getCountries().add(country);
		}
	}

	public void fillNeighboringCountriesInMap(String neighboringCountriesString) {
		String[] neighbouringCountriesList = neighboringCountriesString.split("\\r\\n");

		for (int i = 1; i < neighbouringCountriesList.length; i++) {
			String[] arrayOfCountries = neighbouringCountriesList[i].split(" ");
			int currentCountryNumber = Integer.parseInt(arrayOfCountries[0]);
			for (int j = 1; j < arrayOfCountries.length; j++) {

				int neighbourCountryNumber = Integer.parseInt(arrayOfCountries[j]) - 1;
				Country neighbourCountry = map.getCountries().get(neighbourCountryNumber);
				Country currentCountry = map.getCountries().get(currentCountryNumber - 1);
				currentCountry.getneighbourCountriesToAdd ().add(neighbourCountry);
			}

		}
	}

	public void writeMap(String ouputMapName) throws IOException {

		File mapName = new File(ouputMapName);
		FileWriter fw = new FileWriter(mapName);
		BufferedWriter bw = new BufferedWriter(fw);
		String continentsData = getContinents();
		bw.write(continentsData);
		bw.write("\n");
		bw.write("\n");
		String countriesData = getCountries();
		bw.write(countriesData);
		bw.write("\n");
		bw.write("\n");
		String boundariesData = getBoundaries();
		bw.write(boundariesData);
		bw.close();

	}

	public String getContinents() {
		String continentsDetails = "[continents]";
		for (int i = 0; i < map.getContinents().size(); i++) {
			Continent continent = map.getContinents().get(i);
			String continentDetails = continent.getContinentName() + " " + continent.getContinentControlValue();
			continentsDetails = continentsDetails + "\n" + continentDetails;
		}
		return continentsDetails;
	}

	public String getCountries() {
		String countriesDetails = "[countries]";

		for (int i = 0; i < map.getCountries().size(); i++) {
			Country country = map.getCountries().get(i);
			String countryDetails = (i + 1) + " " + country.getCountryName() + " "
					+ getContinentNumber(country.getContinentName());

			countriesDetails = countriesDetails + "\n" + countryDetails;
		}
		return countriesDetails;
	}

	public String getBoundaries() {
		String boundariesDetails = "[borders]";

		for (int i = 0; i < map.getCountries().size(); i++) {
			String boundaryDetails = i + 1 + " ";

			for (int j = 0; j < map.getCountries().get(i).getneighbourCountriesToAdd ().size(); j++) {
				boundaryDetails = boundaryDetails
						+ getCountryNumber(map.getCountries().get(i).getneighbourCountriesToAdd ().get(j).getCountryName())
						+ " ";
			}
			boundariesDetails = boundariesDetails + "\n" + boundaryDetails;
		}
		return boundariesDetails;
	}

	public void addContinentToMap(String continentName, int continentConrrolValue) {

		Continent continent = new Continent();
		continent.setContinentName(continentName);
		continent.setContinentControlValue(continentConrrolValue);
		map.getContinents().add(continent);
	}

	public void addCountryToMap(String countryName, String continentName) {
		Country country = new Country();
		country.setCountryName(countryName);
		int continentIndex = getContinentNumber(continentName) - 1;
		country.setContinentName(continentName);
		map.getCountries().add(country);
		map.getContinents().get(continentIndex).getListOfCountries().add(country);
	}

	public void addNeighborCountryToMap(String countryName, String neighborCountryName) {
		Country country = null;
		int countryIndex = getCountryNumber(countryName) - 1;
		country = map.getCountries().get(countryIndex);
		Country neighborCountry = null;
		int neighborCountryIndex = getCountryNumber(neighborCountryName) - 1;
		neighborCountry = map.getCountries().get(neighborCountryIndex);
		country.getneighbourCountriesToAdd ().add(neighborCountry);

	}

	public void removeCountryFromMap(String countryName) {

		// Get the country Object to be removed
		int removeCountryIndex = getCountryNumber(countryName) - 1;
		Country removeCountry = map.getCountries().get(removeCountryIndex);

		// 1.Remove the country from the continent List
		int continentNumberIndex = getContinentNumber(removeCountry.getContinentName()) - 1;
		// remove country from continent list
		int removeCountryIndexInContinentList = 0;
		for (int i = 0; i < map.getContinents().get(continentNumberIndex).getListOfCountries().size()
				&& !map.getContinents().get(continentNumberIndex).getListOfCountries().get(i).getCountryName()
						.equals(countryName); i++) {
			removeCountryIndexInContinentList = i;
		}
		if (map.getContinents().get(continentNumberIndex).getListOfCountries().size() >= 1) {
			map.getContinents().get(continentNumberIndex).getListOfCountries()
					.remove(removeCountryIndexInContinentList);
		}

		// 2.Remove Country from neighbor Country List

		// Neighbor Details
		java.util.List<Country> neighborCountries = map.getCountries().get(removeCountryIndex).getneighbourCountriesToAdd ();

		String removeCountryName = removeCountry.getCountryName();

		for (int i = 0; i < neighborCountries.size(); i++) {
			Country neighborCountry = neighborCountries.get(i);
			int removeneighborIndex = 0;
			for (int j = 0; j < neighborCountry.getneighbourCountriesToAdd ().size() && !neighborCountry
					.getneighbourCountriesToAdd ().get(j).getCountryName().equals(removeCountryName); j++) {
				removeneighborIndex = j + 1;
			}
			// remove country from the neighbor country list
			neighborCountry.getneighbourCountriesToAdd ().remove(removeneighborIndex);
		}
		// 3. Last remove the country from the country List

		if (map.getCountries().size() >= 1) {
			// remove country from country list
			map.getCountries().remove(removeCountryIndex);
		}

	}

	public void removeNeighborCountryFromMap(String countryName, String neighborRemoveCountryName) {
		// still need to modify
		int desiredCountryIndex = 0;
		int desiredNeighborIndex = 0;

		for (int i = 0; i < map.getCountries().size()
				&& !map.getCountries().get(i).getCountryName().equals(countryName); i++) {
			desiredCountryIndex = i + 1;
		}
		java.util.List<Country> neighborCountries = map.getCountries().get(desiredCountryIndex).getneighbourCountriesToAdd ();

		for (int i = 0; i < neighborCountries.size()
				&& !neighborCountries.get(i).getCountryName().equals(neighborRemoveCountryName); i++) {
			desiredNeighborIndex = i + 1;
		}

		map.getCountries().get(desiredCountryIndex).getneighbourCountriesToAdd ().remove(desiredNeighborIndex);
	}

	public void removeContinentFromMap(String continentName) {

		int removeContinentIndex = getContinentNumber(continentName) - 1;

		java.util.List<Country> removeCountries = map.getContinents().get(removeContinentIndex).getListOfCountries();

		ArrayList<String> removeCountriesNames = new ArrayList<String>();
		for (int i = 0; i < removeCountries.size(); i++) {
			System.out.println(removeCountries.get(i).getCountryName());
			removeCountriesNames.add(removeCountries.get(i).getCountryName());
		}
		for (int i = 0; i < removeCountriesNames.size(); i++) {
			removeCountryFromMap(removeCountriesNames.get(i));
		}
		map.getContinents().remove(removeContinentIndex);
	}

	public boolean doesCountryExit(String countryName) {
		for (int i = 0; i < map.getCountries().size(); i++) {
			if (map.getCountries().get(i).getCountryName().equals(countryName)) {
				return true;
			}
		}
		return false;
	}

	public boolean doesContinentExit(String continentName) {
		for (int i = 0; i < map.getContinents().size(); i++) {
			if (map.getContinents().get(i).getContinentName().equals(continentName)) {
				return true;
			}
		}
		return false;
	}

}