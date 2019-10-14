package com.appriskgame.services;

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

import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;

public class MapOperations {
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	String workingDir = System.getProperty("user.dir");
	String mapLocation = workingDir + "/resources/maps/";
	GameMap gameMap = new GameMap();

	public boolean isMapExists(String mapFileName) {
		String mapFileNameWithExtention = mapFileName + ".map";
		File mapFolder = new File(mapLocation);
		File[] listFiles = mapFolder.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
//			System.out.println("File Name"+listFiles[i].getName());
			if (mapFileNameWithExtention.equals(listFiles[i].getName())) {
				return true;
			}
		}

		return false;
	}

	public GameMap readGameMap(String inputGameMapName) throws IOException {
		HashMap<String, Country> countrySet = new HashMap<>();
		MapValidation validate = new MapValidation();
		String GameMapName = inputGameMapName;
		boolean uploadSuccessful = false;
		String data = "";
		uploadSuccessful = validate.validateMap(GameMapName);
		if (uploadSuccessful) {
			try {
				data = new String(Files.readAllBytes(Paths.get(GameMapName)));
				String[] requiredData = data.split("name");
				data = requiredData[1];
			} catch (IOException e) {

				e.printStackTrace();
			}
			String[] formattedData = data.split("\\r\\n\\r\\n");
			fillContinentsInGameMap(formattedData[2]);
			fillCountriesInGameMap(formattedData[3]);
			fillNeighboringCountriesInGameMap(formattedData[4]);
			gameMap.getCountries().forEach(country -> {
				countrySet.put(country.getCountryName(), country);
				gameMap.setCountrySet(countrySet);

			});
			return gameMap;
		} else {
			System.out.println(MapValidation.getError());
			System.out.println("\nPlease rectify all the above mentioned issues.");
		}
		return new GameMap();
	}

	public void fillContinentsInGameMap(String ContinentsString) {
		String[] continentList = ContinentsString.split("\\r\\n");

		for (int i = 1; i < continentList.length; i++) {
			String[] ContinentDetails = continentList[i].split(" ");
			Continent continent = new Continent();
			continent.setContinentName(ContinentDetails[0]);
			continent.setContinentControlValue(Integer.parseInt(ContinentDetails[1]));
			gameMap.getContinents().add(continent);
		}
	}

	public int getContinentNumber(String continentName) {
		int cotinenentNumber = 0;

		for (int i = 0; i < gameMap.getContinents().size()
				&& !gameMap.getContinents().get(i).getContinentName().equalsIgnoreCase(continentName); i++) {
			cotinenentNumber = i + 1;
		}
		int actualContinentNumber = cotinenentNumber + 1;

		return actualContinentNumber;
	}

	public String getContinentName(int continentNumber) {
		String cotinenentName = "";

		cotinenentName = gameMap.getContinents().get(continentNumber - 1).getContinentName();

		return cotinenentName;
	}

	public int getCountryNumber(String countryName) {
		int countryNumber = 0;

		for (int i = 0; i < gameMap.getCountries().size()
				&& !gameMap.getCountries().get(i).getCountryName().equalsIgnoreCase(countryName); i++) {
			countryNumber = i + 1;
		}
		int actualCountryNumber = countryNumber + 1;

		return actualCountryNumber;
	}

	public String getCountryName(int countryNumber) {
		String countryName = "";

		countryName = gameMap.getCountries().get(countryNumber).getCountryName();

		return countryName;
	}

	public void fillCountriesInGameMap(String CountriesString) {
		String[] countriesList = CountriesString.split("\\r\\n");

		for (int i = 1; i < countriesList.length; i++) {
			String[] countryDetails = countriesList[i].split(" ");
			String continentName = getContinentName(Integer.parseInt(countryDetails[2]));
			int continentNumber = Integer.parseInt(countryDetails[2]);
			Continent continent = gameMap.getContinents().get(continentNumber - 1);
			Country country = new Country();
			country.setCountryName(countryDetails[1]);
			country.setContinentName(continentName);
			country.setPartOfContinent(continent);
			continent.getListOfCountries().add(country);

			gameMap.getCountries().add(country);
		}
	}

	public void fillNeighboringCountriesInGameMap(String neighboringCountriesString) {
		String[] neighbouringCountriesList = neighboringCountriesString.split("\\r\\n");

		for (int i = 1; i < neighbouringCountriesList.length; i++) {
			String[] arrayOfCountries = neighbouringCountriesList[i].split(" ");
			int currentCountryNumber = Integer.parseInt(arrayOfCountries[0]);
			for (int j = 1; j < arrayOfCountries.length; j++) {

				int neighbourCountryNumber = Integer.parseInt(arrayOfCountries[j]) - 1;
				Country neighbourCountry = gameMap.getCountries().get(neighbourCountryNumber);
				Country currentCountry = gameMap.getCountries().get(currentCountryNumber - 1);
				currentCountry.getNeighbourCountriesToAdd().add(neighbourCountry);
				currentCountry.getNeighbourCountries().add(neighbourCountry.getCountryName());
			}

		}
	}

	public void writeGameMap(String ouputGameMapName, String mapFileName) throws IOException {

		File GameMapName = new File(ouputGameMapName);
		FileWriter fw = new FileWriter(GameMapName);
		BufferedWriter bw = new BufferedWriter(fw);
		String fileData = getFileTags(mapFileName);
		bw.write(fileData);
		bw.write("\r\n\r\n");
		String continentsData = getContinents();
		bw.write(continentsData);
		bw.write("\r\n\r\n");
		String countriesData = getCountries();
		bw.write(countriesData);
		bw.write("\r\n\r\n");
		String boundariesData = getBoundaries();
		bw.write(boundariesData);
		bw.close();
	}

	public String getFileTags(String ouputGameMapName) {
		String mapNameDetails = "\r\n\r\nname " + ouputGameMapName + " Map";
		String fileTag = "\r\n\r\n[files]\r\n";
		String pic = "pic " + ouputGameMapName + "_pic.png";
		String fullFormat = mapNameDetails + fileTag + pic;
		return fullFormat;
	}

	public String getContinents() {
		String continentsDetails = "[continents]";
		for (int i = 0; i < gameMap.getContinents().size(); i++) {
			Continent continent = gameMap.getContinents().get(i);
			String continentDetails = continent.getContinentName() + " " + continent.getContinentControlValue() + " "
					+ "#99NoColor";
			continentsDetails = continentsDetails + "\r\n" + continentDetails;
		}
		return continentsDetails;
	}

	public String getCountries() {
		String countriesDetails = "[countries]";

		for (int i = 0; i < gameMap.getCountries().size(); i++) {
			Country country = gameMap.getCountries().get(i);
			String countryDetails = (i + 1) + " " + country.getCountryName() + " "
					+ getContinentNumber(country.getContinentName()) + " " + "99" + " " + "99";

			countriesDetails = countriesDetails + "\r\n" + countryDetails;
		}
		return countriesDetails;
	}

	public String getBoundaries() {
		String boundariesDetails = "[borders]";

		for (int i = 0; i < gameMap.getCountries().size(); i++) {
			String boundaryDetails = i + 1 + " ";

			for (int j = 0; j < gameMap.getCountries().get(i).getNeighbourCountriesToAdd().size(); j++) {
				boundaryDetails = boundaryDetails
						+ getCountryNumber(
								gameMap.getCountries().get(i).getNeighbourCountriesToAdd().get(j).getCountryName())
						+ " ";
			}
			boundariesDetails = boundariesDetails + "\r\n" + boundaryDetails;
		}
		return boundariesDetails;
	}

	public void addContinentToGameMap(String continentName, int continentConrrolValue) {

		Continent continent = new Continent();
		continent.setContinentName(continentName);
		continent.setContinentControlValue(continentConrrolValue);
		gameMap.getContinents().add(continent);
	}

	public void addCountryToGameMap(String countryName, String continentName) {
		Country country = new Country();
		country.setCountryName(countryName);
		int continentIndex = getContinentNumber(continentName) - 1;
		country.setContinentName(continentName);
		Continent continent = gameMap.getContinents().get(continentIndex);
		country.setPartOfContinent(continent);
		gameMap.getCountries().add(country);
		gameMap.getContinents().get(continentIndex).getListOfCountries().add(country);
	}

	public void addNeighborCountryToGameMap(String countryName, String neighborCountryName) {
		Country country = null;
		int countryIndex = getCountryNumber(countryName) - 1;
		country = gameMap.getCountries().get(countryIndex);
		Country neighborCountry = null;
		int neighborCountryIndex = getCountryNumber(neighborCountryName) - 1;
		neighborCountry = gameMap.getCountries().get(neighborCountryIndex);
		country.getNeighbourCountriesToAdd().add(neighborCountry);
		country.getNeighbourCountries().add(neighborCountry.getCountryName());

	}

	public void removeCountryFromGameMap(String countryName) {

		// Get the country Object to be removed
		int removeCountryIndex = getCountryNumber(countryName) - 1;
		Country removeCountry = gameMap.getCountries().get(removeCountryIndex);

		// 1.Remove the country from the continent List
		int continentNumberIndex = getContinentNumber(removeCountry.getContinentName()) - 1;
		// remove country from continent list
		int removeCountryIndexInContinentList = 0;
		for (int i = 0; i < gameMap.getContinents().get(continentNumberIndex).getListOfCountries().size()
				&& !gameMap.getContinents().get(continentNumberIndex).getListOfCountries().get(i).getCountryName()
						.equalsIgnoreCase(countryName); i++) {
			removeCountryIndexInContinentList = i;
		}
		if (gameMap.getContinents().get(continentNumberIndex).getListOfCountries().size() >= 1) {
			gameMap.getContinents().get(continentNumberIndex).getListOfCountries()
					.remove(removeCountryIndexInContinentList);
		}

		// 2.Remove Country from neighbor Country List

		// Neighbor Details
		java.util.List<Country> neighborCountries = gameMap.getCountries().get(removeCountryIndex)
				.getNeighbourCountriesToAdd();

		String removeCountryName = removeCountry.getCountryName();

		for (int i = 0; i < neighborCountries.size(); i++) {
			Country neighborCountry = neighborCountries.get(i);
			int removeneighborIndex = 0;
			for (int j = 0; j < neighborCountry.getNeighbourCountriesToAdd().size() && !neighborCountry
					.getNeighbourCountriesToAdd().get(j).getCountryName().equalsIgnoreCase(removeCountryName); j++) {
				removeneighborIndex = j + 1;
			}
			// remove country from the neighbor country list
			neighborCountry.getNeighbourCountriesToAdd().remove(removeneighborIndex);
			neighborCountry.getNeighbourCountries().remove(removeneighborIndex);
		}
		// 3. Last remove the country from the country List

		if (gameMap.getCountries().size() >= 1) {
			// remove country from country list
			gameMap.getCountries().remove(removeCountryIndex);
		}

	}

	public void removeNeighborCountryFromGameMap(String countryName, String neighborRemoveCountryName) {
		// still need to modify
		int desiredCountryIndex = 0;
		int desiredNeighborIndex = 0;

		for (int i = 0; i < gameMap.getCountries().size()
				&& !gameMap.getCountries().get(i).getCountryName().equalsIgnoreCase(countryName); i++) {
			desiredCountryIndex = i + 1;
		}
		java.util.List<Country> neighborCountries = gameMap.getCountries().get(desiredCountryIndex)
				.getNeighbourCountriesToAdd();

		for (int i = 0; i < neighborCountries.size()
				&& !neighborCountries.get(i).getCountryName().equalsIgnoreCase(neighborRemoveCountryName); i++) {
			desiredNeighborIndex = i + 1;
		}

		gameMap.getCountries().get(desiredCountryIndex).getNeighbourCountriesToAdd().remove(desiredNeighborIndex);
		gameMap.getCountries().get(desiredNeighborIndex).getNeighbourCountries().remove(desiredNeighborIndex);
	}

	public void removeContinentFromGameMap(String continentName) {

		int removeContinentIndex = getContinentNumber(continentName) - 1;

		java.util.List<Country> removeCountries = gameMap.getContinents().get(removeContinentIndex)
				.getListOfCountries();

		ArrayList<String> removeCountriesNames = new ArrayList<String>();
		for (int i = 0; i < removeCountries.size(); i++) {
//			System.out.println(removeCountries.get(i).getCountryName());
			removeCountriesNames.add(removeCountries.get(i).getCountryName());
		}
		for (int i = 0; i < removeCountriesNames.size(); i++) {
			removeCountryFromGameMap(removeCountriesNames.get(i));
		}
		gameMap.getContinents().remove(removeContinentIndex);
	}

	public boolean doesCountryExit(String countryName) {
		for (int i = 0; i < gameMap.getCountries().size(); i++) {
			if (gameMap.getCountries().get(i).getCountryName().equalsIgnoreCase(countryName)) {
				return true;
			}
		}
		return false;
	}

	public boolean doesContinentExit(String continentName) {
		for (int i = 0; i < gameMap.getContinents().size(); i++) {
			if (gameMap.getContinents().get(i).getContinentName().equalsIgnoreCase(continentName)) {
				return true;
			}
		}
		return false;
	}

	public void showmapDetails() {

		System.out.println("Continents");
		System.out.println("----------");
		for (int i = 0; i < gameMap.getContinents().size(); i++) {
			System.out.println(gameMap.getContinents().get(i).getContinentName());
		}
		System.out.println();
		System.out.println("Countries");
		System.out.println("----------");
		for (int i = 0; i < gameMap.getCountries().size(); i++) {
			System.out.print(gameMap.getCountries().get(i).getCountryName());
			System.out.println(gameMap.getCountries().get(i).getNeighbourCountries());
		}

	}

	public boolean isCountryUnique(String countryName) {
		for (int i = 0; i < gameMap.getCountries().size(); i++) {
			if (gameMap.getCountries().get(i).getCountryName().equalsIgnoreCase(countryName)) {
				return false;
			}
		}
		return true;
	}

	public boolean isContinentUnique(String continentName) {
		for (int i = 0; i < gameMap.getContinents().size(); i++) {
			if (gameMap.getContinents().get(i).getContinentName().equalsIgnoreCase(continentName)) {
				return false;
			}
		}
		return true;
	}

	public boolean isBorderUnique(String countryName, String neighborCountryName) {
		int desiredCountryIndex = getCountryNumber(countryName) - 1;
		Country currentCountry = gameMap.getCountries().get(desiredCountryIndex);
		java.util.List<Country> neighborCountries = currentCountry.getNeighbourCountriesToAdd();
		for (int i = 0; i < neighborCountries.size(); i++) {
			if (neighborCountries.get(i).getCountryName().equalsIgnoreCase(neighborCountryName)) {
				return false;
			}
		}
		return true;
	}

	public boolean isContinentRuleSatisfied() {
		if (gameMap.getContinents().size() > 2) {
			return true;
		} else {
			return false;
		}
	}

	private void validateMapDetails() {
		MapValidation validate = new MapValidation();
		String continentsData = getContinents();
		String countriesData = getCountries();
		String boundariesData = getBoundaries();
		validate.validateContinents(continentsData);
		validate.validateCountries(countriesData);
		validate.validateBoundaries(boundariesData);
	}

	public GameMap editMap() throws IOException {

		boolean flag = true;
		while (flag) {
			System.out.println("Enter the command : ");
			String command = br.readLine().trim();
			String[] cmdDetails = command.split(" ");
			String cmdType = cmdDetails[0];
			String opsType = "";
			if (cmdDetails.length > 1) {
				opsType = cmdDetails[1];
			}
			if (cmdType.equals("editcontinent")) {

				if (opsType.equals("-add")) {

					if (isContinentUnique(cmdDetails[2])) {
						addContinentToGameMap(cmdDetails[2], Integer.parseInt(cmdDetails[3]));
						System.out.println("Do you want to perform other map operations? Yes/No");
						String choice = br.readLine().trim();
						while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No") || choice == null)) {
							System.err.println("\nPlease enter the choice as either Yes or No:");
							choice = br.readLine().trim();
						}

						if (choice.equalsIgnoreCase("Yes")) {
							flag = true;
						} else {
							flag = false;
						}
					} else {
						System.out.println(cmdDetails[2] + "  " + "Continent is Already Present in the GameMap");
						flag = true;
					}

				} else if (opsType.equals("-remove")) {
					if (isContinentRuleSatisfied()) {

						if (doesContinentExit(cmdDetails[2])) {
							removeContinentFromGameMap(cmdDetails[2]);
							System.out.println("Do you want to perform other map operations? Yes/No");
							String choice = br.readLine().trim();
							while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No")
									|| choice == null)) {
								System.err.println("\nPlease enter the choice as either Yes or No:");
								choice = br.readLine().trim();
							}

							if (choice.equalsIgnoreCase("Yes")) {
								flag = true;
							} else {
								flag = false;
							}
						} else {
							System.out.println(cmdDetails[2] + "  " + "Continent is not present in the GameMap");
							flag = true;
						}

					} else {
						System.out.println("There should be atleast 2 Continents in the GameMap");
					}

				}

			} else if (cmdType.equals("editcountry")) {
				if (opsType.equals("-add")) {

					if (doesContinentExit(cmdDetails[3])) {

						if (isCountryUnique(cmdDetails[2])) {
							addCountryToGameMap(cmdDetails[2], cmdDetails[3]);
							System.out.println("Do you want to perform other map operations? Yes/No");
							String choice = br.readLine().trim();
							while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No")
									|| choice == null)) {
								System.err.println("\nPlease enter the choice as either Yes or No:");
								choice = br.readLine().trim();
							}

							if (choice.equalsIgnoreCase("Yes")) {
								flag = true;
							} else {
								flag = false;
							}
						} else {
							System.out.println(cmdDetails[2] + "  " + "Country is Already Present in the GameMap");
							flag = true;
						}

					} else {
						System.out.println(cmdDetails[3] + "Continent is not present in the GameMap");
						flag = true;
					}

				} else if (opsType.equals("-remove")) {
					if (cmdDetails.length == 3) {

						if (doesCountryExit(cmdDetails[2])) {
							removeCountryFromGameMap(cmdDetails[2]);
							System.out.println("Do you want to perform other map operations? Yes/No");
							String choice = br.readLine().trim();
							while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No")
									|| choice == null)) {
								System.err.println("\nPlease enter the choice as either Yes or No:");
								choice = br.readLine().trim();
							}

							if (choice.equalsIgnoreCase("Yes")) {
								flag = true;
							} else {
								flag = false;
							}
						} else {
							System.out.println(cmdDetails[2] + "  " + "Country is not present in the GameMap");
							flag = true;
						}

					}

				}

			} else if (cmdType.equals("editneighbor")) {

				if (opsType.equals("-add")) {
					if (doesCountryExit(cmdDetails[2])) {
						if (doesCountryExit(cmdDetails[3])) {

							if (isBorderUnique(cmdDetails[2], cmdDetails[3])) {
								addNeighborCountryToGameMap(cmdDetails[2], cmdDetails[3]);
								addNeighborCountryToGameMap(cmdDetails[3], cmdDetails[2]);
								System.out.println("Do you want to perform other map operations? Yes/No");
								String choice = br.readLine().trim();
								while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No")
										|| choice == null)) {
									System.err.println("\nPlease enter the choice as either Yes or No:");
									choice = br.readLine().trim();
								}

								if (choice.equalsIgnoreCase("Yes")) {
									flag = true;
								} else {
									flag = false;
								}
							} else {
								System.out.println(cmdDetails[2] + cmdDetails[3] + "  "
										+ "Boundary is Already present in the GameMap");
								flag = true;
							}
						} else {
							System.out.println(cmdDetails[3] + "  " + "Country is not present in the GameMap");
							flag = true;
						}
					} else {
						System.out.println(cmdDetails[2] + "  " + "Country is not present in the GameMap");
						flag = true;
					}

				} else if (opsType.equals("-remove")) {

					if (doesCountryExit(cmdDetails[2])) {
						if (doesCountryExit(cmdDetails[3])) {
							removeNeighborCountryFromGameMap(cmdDetails[2], cmdDetails[3]);
							// Remove the same in other way
							removeNeighborCountryFromGameMap(cmdDetails[3], cmdDetails[2]);
							System.out.println("Do you want to perform other map operations? Yes/No");
							String choice = br.readLine().trim();
							while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No")
									|| choice == null)) {
								System.err.println("\nPlease enter the choice as either Yes or No:");
								choice = br.readLine().trim();
							}

							if (choice.equalsIgnoreCase("Yes")) {
								flag = true;
							} else {
								flag = false;
							}
						} else {
							System.out.println(cmdDetails[3] + "  " + "Country is not present in the GameMap");
							flag = true;
						}
					} else {
						System.out.println(cmdDetails[2] + "  " + "Country is not present in the GameMap");
						flag = true;
					}

				}
			} else if (cmdType.equals("showmap")) {
				showmapDetails();
				System.out.println("Do you want to perform other map operations? Yes/No");
				String choice = br.readLine().trim();
				while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No") || choice == null)) {
					System.err.println("\nPlease enter the choice as either Yes or No:");
					choice = br.readLine().trim();
				}

				if (choice.equalsIgnoreCase("Yes")) {
					flag = true;
				} else {
					flag = false;
				}
			} else if (cmdType.equals("validatemap")) {
				validateMapDetails();
				System.out.println("Do you want to perform other map operations? Yes/No");
				String choice = br.readLine().trim();
				while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No") || choice == null)) {
					System.err.println("\nPlease enter the choice as either Yes or No:");
					choice = br.readLine().trim();
				}

				if (choice.equalsIgnoreCase("Yes")) {
					flag = true;
				} else {
					flag = false;
				}

			}

		}
		System.out.println("Do you want to Save the Map File? Yes/No");
		String choice = br.readLine().trim();
		while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No") || choice == null)) {
			System.err.println("\nPlease enter the choice as either Yes or No:");
			choice = br.readLine().trim();
		}

		if (choice.equalsIgnoreCase("Yes")) {
			// validate
			System.out.println("Enter the command to save the Map File");
			String command = br.readLine().trim();
			String[] cmdDetails = command.split(" ");
			String cmdType = cmdDetails[0];
			if (cmdType.equals("savemap")) {
				if (cmdDetails.length == 2) {
					String fileName = cmdDetails[1];
					String ouputGameMapName = mapLocation + fileName + ".map";
					writeGameMap(ouputGameMapName, fileName);
					MapValidation validate = new MapValidation();
					boolean uploadSuccessful = false;
					uploadSuccessful = validate.validateMap(ouputGameMapName);
					if (uploadSuccessful) {
						System.out.println("Successfully Saved");
					} else {
						File file = new File(ouputGameMapName);
						file.delete();
						System.out.println(MapValidation.getError());
						System.out.println("\nPlease rectify all the above mentioned issues");
					}
				} else {
					System.out.println("Incorrect command");
				}
			} else {
				GameMap map = new GameMap();
				return map;
			}
		}
		return gameMap;

	}

	public GameMap createFile() throws IOException {
		GameMap map = editMap();
		return map;

	}

	public GameMap loadFile() throws IOException {
		boolean flag = true;
		while (flag) {
			flag = false;
			System.out.println("Enter the command to Edit the existing Map File");
			String command = br.readLine().trim();
			String[] cmdDetails = command.split(" ");
			String cmdType = cmdDetails[0];
			GameMap gameMapCheck;
			if (cmdType.equals("editmap")) {
				if (cmdDetails.length == 2) {
					String mapFileName = cmdDetails[1];
					if (isMapExists(mapFileName)) {
						String inputGameMapName = mapLocation + mapFileName + ".map";
						gameMapCheck = readGameMap(inputGameMapName);
						if (gameMapCheck.getContinents().isEmpty()) {
							flag = true;
							System.out.println("Incorrect File");
						}
					} else {
						System.out.println("Do you want to create a map from scratch? Yes/No");
						String choice = br.readLine().trim();
						while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No") || choice == null)) {
							System.err.println("\nPlease enter the choice as either Yes or No:");
							choice = br.readLine().trim();
						}

						if (choice.equalsIgnoreCase("Yes")) {
							createFile();
						} else {
							loadFile();
						}

					}
					if (!flag) {
						System.out.println("Do you want to edit the loaded map? Yes/No");
						String choice = br.readLine().trim();
						while (!(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No") || choice == null)) {
							System.err.println("\nPlease enter the choice as either Yes or No:");
							choice = br.readLine().trim();
						}

						if (choice.equalsIgnoreCase("Yes")) {
							GameMap map = editMap();
							return map;
						} else {
							return gameMap;
						}
					}
				}

			} else {
				System.out.println("Incorrect Command");
				flag = true;
			}
		}
		return gameMap;

	}
}