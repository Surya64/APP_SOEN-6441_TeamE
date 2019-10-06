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
import com.appriskgame.model.GameMap;

public class MapOperations {

	GameMap GameMap = new GameMap();

	public static void main(String[] args) throws IOException {

		MapOperations loadGameMap = new MapOperations();
//		For Read and write the GameMap
		String inputGameMapName = "C:\\Users\\saich\\Desktop\\Test\\ameroki.map";
		String ouputGameMapName = "C:\\Users\\saich\\Desktop\\Result\\out.map";
		loadGameMap.readGameMap(inputGameMapName);
		loadGameMap.printDetails();

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
					loadGameMap.addContinentToGameMap(cmdDetails[2], Integer.parseInt(cmdDetails[3]));
				} else if (opsType.equals("-remove")) {

					if (loadGameMap.doesContinentExit(cmdDetails[2])) {
						loadGameMap.removeContinentFromGameMap(cmdDetails[2]);
					} else {
						System.out.println(cmdDetails[2] + "  " + "Continent is not present in the GameMap");
					}

				}

			} else if (cmdType.equals("editcountry")) {
				if (opsType.equals("-add")) {

					if (loadGameMap.doesContinentExit(cmdDetails[3])) {
						loadGameMap.addCountryToGameMap(cmdDetails[2], cmdDetails[3]);
					} else {
						System.out.println(cmdDetails[3] + "Continent is not present in the GameMap");
					}

				} else if (opsType.equals("-remove")) {
					if (cmdDetails.length == 3) {

						if (loadGameMap.doesCountryExit(cmdDetails[2])) {
							loadGameMap.removeCountryFromGameMap(cmdDetails[2]);
						} else {
							System.out.println(cmdDetails[2] + "  " + "Country is not present in the GameMap");
						}

					}

				}

			} else if (cmdType.equals("editneighbor")) {

				if (opsType.equals("-add")) {
					if (loadGameMap.doesCountryExit(cmdDetails[2])) {
						if (loadGameMap.doesCountryExit(cmdDetails[3])) {
							loadGameMap.addNeighborCountryToGameMap(cmdDetails[2], cmdDetails[3]);
							loadGameMap.addNeighborCountryToGameMap(cmdDetails[3], cmdDetails[2]);
						} else {
							System.out.println(cmdDetails[3] + "  " + "Country is not present in the GameMap");
						}
					} else {
						System.out.println(cmdDetails[2] + "  " + "Country is not present in the GameMap");
					}

				} else if (opsType.equals("-remove")) {

					if (loadGameMap.doesCountryExit(cmdDetails[2])) {
						if (loadGameMap.doesCountryExit(cmdDetails[3])) {
							loadGameMap.removeNeighborCountryFromGameMap(cmdDetails[2], cmdDetails[3]);
							// Remove the same in other way
							loadGameMap.removeNeighborCountryFromGameMap(cmdDetails[3], cmdDetails[2]);
						} else {
							System.out.println(cmdDetails[3] + "  " + "Country is not present in the GameMap");
						}
					} else {
						System.out.println(cmdDetails[2] + "  " + "Country is not present in the GameMap");
					}

				}
			}

			System.out.println("Enter 1 to continue");
			ops = sc.nextInt();
		}

		// Write to File
		loadGameMap.writeGameMap(ouputGameMapName);
		loadGameMap.printDetails();
		System.out.println();

	}

	public void readGameMap(String inputGameMapName) {
		String data = "";
		String GameMapName = inputGameMapName;
		try {
			data = new String(Files.readAllBytes(Paths.get(GameMapName)));
		} catch (IOException e) {

			e.printStackTrace();
		}
		String[] formattedData = data.split("\\r\\n\\r\\n");

		fillContinentsInGameMap(formattedData[3]);
		fillCountriesInGameMap(formattedData[4]);
		fillNeighboringCountriesInGameMap(formattedData[5]);

	}

	public void fillContinentsInGameMap(String ContinentsString) {
		String[] continentList = ContinentsString.split("\\r\\n");

		for (int i = 1; i < continentList.length; i++) {
			String[] ContinentDetails = continentList[i].split(" ");
			Continent continent = new Continent();
			continent.setContinentName(ContinentDetails[0]);
			continent.setContinentControlValue(Integer.parseInt(ContinentDetails[1]));
			GameMap.getContinents().add(continent);
		}
	}

	public int getContinentNumber(String continentName) {
		int cotinenentNumber = 0;

		for (int i = 0; i < GameMap.getContinents().size()
				&& !GameMap.getContinents().get(i).getContinentName().equals(continentName); i++) {
			cotinenentNumber = i + 1;
		}
		int actualContinentNumber = cotinenentNumber + 1;

		return actualContinentNumber;
	}

	public String getContinentName(int continentNumber) {
		String cotinenentName = "";

		cotinenentName = GameMap.getContinents().get(continentNumber - 1).getContinentName();

		return cotinenentName;
	}

	public int getCountryNumber(String countryName) {
		int countryNumber = 0;

		for (int i = 0; i < GameMap.getCountries().size()
				&& !GameMap.getCountries().get(i).getCountryName().equals(countryName); i++) {
			countryNumber = i + 1;
		}
		int actualCountryNumber = countryNumber + 1;

		return actualCountryNumber;
	}

	public String getCountryName(int countryNumber) {
		String countryName = "";

		countryName = GameMap.getCountries().get(countryNumber).getCountryName();

		return countryName;
	}

	public void fillCountriesInGameMap(String CountriesString) {
		String[] countriesList = CountriesString.split("\\r\\n");

		for (int i = 1; i < countriesList.length; i++) {
			String[] countryDetails = countriesList[i].split(" ");
			String continentName = getContinentName(Integer.parseInt(countryDetails[2]));
			int continentNumber = Integer.parseInt(countryDetails[2]);
			Continent continent = GameMap.getContinents().get(continentNumber - 1);
			Country country = new Country();
			country.setCountryName(countryDetails[1]);
			country.setContinentName(continentName);
			continent.getListOfCountries().add(country);

			GameMap.getCountries().add(country);
		}
	}

	public void fillNeighboringCountriesInGameMap(String neighboringCountriesString) {
		String[] neighbouringCountriesList = neighboringCountriesString.split("\\r\\n");

		for (int i = 1; i < neighbouringCountriesList.length; i++) {
			String[] arrayOfCountries = neighbouringCountriesList[i].split(" ");
			int currentCountryNumber = Integer.parseInt(arrayOfCountries[0]);
			for (int j = 1; j < arrayOfCountries.length; j++) {

				int neighbourCountryNumber = Integer.parseInt(arrayOfCountries[j]) - 1;
				Country neighbourCountry = GameMap.getCountries().get(neighbourCountryNumber);
				Country currentCountry = GameMap.getCountries().get(currentCountryNumber - 1);
				currentCountry.getneighbourCountriesToAdd().add(neighbourCountry);
				currentCountry.getNeighbourCountries().add(neighbourCountry.getCountryName());
			}

		}
	}

	public void writeGameMap(String ouputGameMapName) throws IOException {

		File GameMapName = new File(ouputGameMapName);
		FileWriter fw = new FileWriter(GameMapName);
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
		for (int i = 0; i < GameMap.getContinents().size(); i++) {
			Continent continent = GameMap.getContinents().get(i);
			String continentDetails = continent.getContinentName() + " " + continent.getContinentControlValue();
			continentsDetails = continentsDetails + "\n" + continentDetails;
		}
		return continentsDetails;
	}

	public String getCountries() {
		String countriesDetails = "[countries]";

		for (int i = 0; i < GameMap.getCountries().size(); i++) {
			Country country = GameMap.getCountries().get(i);
			String countryDetails = (i + 1) + " " + country.getCountryName() + " "
					+ getContinentNumber(country.getContinentName());

			countriesDetails = countriesDetails + "\n" + countryDetails;
		}
		return countriesDetails;
	}

	public String getBoundaries() {
		String boundariesDetails = "[borders]";

		for (int i = 0; i < GameMap.getCountries().size(); i++) {
			String boundaryDetails = i + 1 + " ";

			for (int j = 0; j < GameMap.getCountries().get(i).getneighbourCountriesToAdd().size(); j++) {
				boundaryDetails = boundaryDetails
						+ getCountryNumber(
								GameMap.getCountries().get(i).getneighbourCountriesToAdd().get(j).getCountryName())
						+ " ";
			}
			boundariesDetails = boundariesDetails + "\n" + boundaryDetails;
		}
		return boundariesDetails;
	}

	public void addContinentToGameMap(String continentName, int continentConrrolValue) {

		Continent continent = new Continent();
		continent.setContinentName(continentName);
		continent.setContinentControlValue(continentConrrolValue);
		GameMap.getContinents().add(continent);
	}

	public void addCountryToGameMap(String countryName, String continentName) {
		Country country = new Country();
		country.setCountryName(countryName);
		int continentIndex = getContinentNumber(continentName) - 1;
		country.setContinentName(continentName);
		GameMap.getCountries().add(country);
		GameMap.getContinents().get(continentIndex).getListOfCountries().add(country);
	}

	public void addNeighborCountryToGameMap(String countryName, String neighborCountryName) {
		Country country = null;
		int countryIndex = getCountryNumber(countryName) - 1;
		country = GameMap.getCountries().get(countryIndex);
		Country neighborCountry = null;
		int neighborCountryIndex = getCountryNumber(neighborCountryName) - 1;
		neighborCountry = GameMap.getCountries().get(neighborCountryIndex);
		country.getneighbourCountriesToAdd().add(neighborCountry);
		country.getNeighbourCountries().add(neighborCountry.getCountryName());

	}

	public void removeCountryFromGameMap(String countryName) {

		// Get the country Object to be removed
		int removeCountryIndex = getCountryNumber(countryName) - 1;
		Country removeCountry = GameMap.getCountries().get(removeCountryIndex);

		// 1.Remove the country from the continent List
		int continentNumberIndex = getContinentNumber(removeCountry.getContinentName()) - 1;
		// remove country from continent list
		int removeCountryIndexInContinentList = 0;
		for (int i = 0; i < GameMap.getContinents().get(continentNumberIndex).getListOfCountries().size()
				&& !GameMap.getContinents().get(continentNumberIndex).getListOfCountries().get(i).getCountryName()
						.equals(countryName); i++) {
			removeCountryIndexInContinentList = i;
		}
		if (GameMap.getContinents().get(continentNumberIndex).getListOfCountries().size() >= 1) {
			GameMap.getContinents().get(continentNumberIndex).getListOfCountries()
					.remove(removeCountryIndexInContinentList);
		}

		// 2.Remove Country from neighbor Country List

		// Neighbor Details
		java.util.List<Country> neighborCountries = GameMap.getCountries().get(removeCountryIndex)
				.getneighbourCountriesToAdd();

		String removeCountryName = removeCountry.getCountryName();

		for (int i = 0; i < neighborCountries.size(); i++) {
			Country neighborCountry = neighborCountries.get(i);
			int removeneighborIndex = 0;
			for (int j = 0; j < neighborCountry.getneighbourCountriesToAdd().size() && !neighborCountry
					.getneighbourCountriesToAdd().get(j).getCountryName().equals(removeCountryName); j++) {
				removeneighborIndex = j + 1;
			}
			// remove country from the neighbor country list
			neighborCountry.getneighbourCountriesToAdd().remove(removeneighborIndex);
			neighborCountry.getNeighbourCountries().remove(removeCountryIndex);
		}
		// 3. Last remove the country from the country List

		if (GameMap.getCountries().size() >= 1) {
			// remove country from country list
			GameMap.getCountries().remove(removeCountryIndex);
		}

	}

	public void removeNeighborCountryFromGameMap(String countryName, String neighborRemoveCountryName) {
		// still need to modify
		int desiredCountryIndex = 0;
		int desiredNeighborIndex = 0;

		for (int i = 0; i < GameMap.getCountries().size()
				&& !GameMap.getCountries().get(i).getCountryName().equals(countryName); i++) {
			desiredCountryIndex = i + 1;
		}
		java.util.List<Country> neighborCountries = GameMap.getCountries().get(desiredCountryIndex)
				.getneighbourCountriesToAdd();

		for (int i = 0; i < neighborCountries.size()
				&& !neighborCountries.get(i).getCountryName().equals(neighborRemoveCountryName); i++) {
			desiredNeighborIndex = i + 1;
		}

		GameMap.getCountries().get(desiredCountryIndex).getneighbourCountriesToAdd().remove(desiredNeighborIndex);
		GameMap.getCountries().get(desiredNeighborIndex).getNeighbourCountries().remove(desiredNeighborIndex);
	}

	public void removeContinentFromGameMap(String continentName) {

		int removeContinentIndex = getContinentNumber(continentName) - 1;

		java.util.List<Country> removeCountries = GameMap.getContinents().get(removeContinentIndex)
				.getListOfCountries();

		ArrayList<String> removeCountriesNames = new ArrayList<String>();
		for (int i = 0; i < removeCountries.size(); i++) {
			System.out.println(removeCountries.get(i).getCountryName());
			removeCountriesNames.add(removeCountries.get(i).getCountryName());
		}
		for (int i = 0; i < removeCountriesNames.size(); i++) {
			removeCountryFromGameMap(removeCountriesNames.get(i));
		}
		GameMap.getContinents().remove(removeContinentIndex);
	}

	public boolean doesCountryExit(String countryName) {
		for (int i = 0; i < GameMap.getCountries().size(); i++) {
			if (GameMap.getCountries().get(i).getCountryName().equals(countryName)) {
				return true;
			}
		}
		return false;
	}

	public boolean doesContinentExit(String continentName) {
		for (int i = 0; i < GameMap.getContinents().size(); i++) {
			if (GameMap.getContinents().get(i).getContinentName().equals(continentName)) {
				return true;
			}
		}
		return false;
	}

	public void printDetails() {

		System.out.println("Continents");
		System.out.println("----------");
		for (int i = 0; i < GameMap.getContinents().size(); i++) {
			System.out.println(GameMap.getContinents().get(i).getContinentName());
		}

		System.out.println("Countries");
		System.out.println("----------");
		for (int i = 0; i < GameMap.getCountries().size(); i++) {
			System.out.print(GameMap.getCountries().get(i).getCountryName());
			System.out.println(GameMap.getCountries().get(i).getNeighbourCountries());
		}

	}

}