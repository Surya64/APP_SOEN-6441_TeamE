package com.appriskgame.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;

/**
 * This class is used to check the format of List of continents, List of
 * countries, adjacency between countries which are present in the uploaded file
 * and validate the map.
 * 
 * @author Surya
 * @author Sai
 */
public class MapValidation {
	private GameMap gameMap;
	private ArrayList<Country> listOfCountries;
	private ArrayList<Continent> listOfContinent;
	private static String errorMessage, adjancencyError;

	/**
	 * This Method used to get the error message.
	 * 
	 * @return errorMessage
	 */
	public static String getError() {
		return errorMessage;
	}

	/**
	 * This Method used to get the Game Map.
	 * 
	 * @return gameMap
	 */
	public GameMap getMapGraph() {
		return gameMap;
	}

	/**
	 * This method is used to validate the list of continents which are present in
	 * the uploaded file and check the correct format.
	 * 
	 * @param tagData Value of the continent tag
	 * @return flag to check if entered continents are valid
	 */
	public boolean validateContinents(String tagData) {

		listOfContinent = new ArrayList<Continent>();
		HashMap<String, String> visitedContinent = new HashMap<String, String>();
		boolean duplicateData = true, formatData = true;
		String formatError = new String();
		String DuplicateError = new String();
		String[] metaData = tagData.split("\r\n");
		if (metaData.length != 1) {
			for (int i = 1; i < metaData.length; i++) {
				String data = metaData[i].trim().toUpperCase();
				Pattern pattern = Pattern.compile("[a-zA-Z-]+ [0-9]+ [a-zA-Z0-9#]+");
				if (!data.trim().isEmpty()) {
					Continent continent = new Continent();
					Matcher match = pattern.matcher(data.trim());
					if (!match.matches()) {
						formatError = formatError
								.concat("Invalid Continent Details. " + data.trim() + " is not the correct format.\n");
						formatData = false;
					} else {
						if (visitedContinent.containsKey(data.split(" ")[0])) {
							DuplicateError = DuplicateError
									.concat(data.split(" ")[0] + " is already defined. Duplicate Entry Found.\n");
							duplicateData = false;
						} else {
							String field = data.split(" ")[0];
							int value = Integer.parseInt(data.split(" ")[1]);
							continent.setContinentName(field);
							continent.setContinentControlValue(value);
							listOfContinent.add(continent);
							visitedContinent.put(field, data);
						}
					}
				}
			}
			if (duplicateData && formatData) {
				return true;
			} else {
				errorMessage = errorMessage.concat(formatError).concat(DuplicateError);
				return false;
			}
		} else {
			errorMessage = errorMessage.concat("No Continents defined under Countinent tag.\n");
			return false;
		}
	}

	/**
	 * This method is used to validate the list of countries which are present in
	 * the uploaded file and check the correct format.
	 * 
	 * @param tagData Value of the country tag
	 * @return flag to check if entered countries are valid
	 */
	public boolean validateCountries(String tagData) {
		listOfCountries = new ArrayList<Country>();
		HashMap<String, String> visited = new HashMap<String, String>();
		boolean duplicateData = true, formatData = true, continentData = true;
		String formatError = new String(), DuplicateError = new String(), continentError = new String();
		String[] countryData = tagData.split("\\r\\n");
		for (int i = 1; i < countryData.length; i++) {
			String data = countryData[i].trim().toUpperCase();
			Pattern pattern = Pattern.compile("[0-9]+ [a-zA-Z_-]+ [0-9]+ [0-9 ]*");
			if (!data.trim().isEmpty()) {
				Matcher match = pattern.matcher(data.trim());
				if (!match.matches()) {
					formatError = formatError.concat(
							"Invalid Country Details. " + data.trim() + " is not defined in required format.\n");
					formatData = false;
					continue;
				} else {
					if (visited.containsKey(data.split(" ")[1])) {
						DuplicateError = DuplicateError
								.concat(data.split(" ")[1] + " is already defined. Duplicate Entry Found.\n");
						duplicateData = false;
						continue;
					} else {
						Country country = new Country();
						String[] countrydetail = data.split(" ");
						String name = countrydetail[1];

						boolean continentAvailable = false;
						if (listOfContinent != null && !listOfContinent.isEmpty()) {
							Continent newcontinent;
							int continentValue = Integer.parseInt(data.split(" ")[2]);

							if (continentValue <= listOfContinent.size()) {
								newcontinent = listOfContinent.get(continentValue - 1);
								country.setPartOfContinent(newcontinent);
								continentAvailable = true;
								continentData = true;
							}
						} else {
							continentError = continentError.concat("No Valid continents available.\n");
							continentData = false;
							break;
						}
						if (!continentAvailable) {
							continentError = continentError
									.concat(data.split(" ")[2] + " is not defined as Continent.\n");
							continentData = false;
							continue;
						}
						country.setCountryName(name);
						listOfCountries.add(country);
						visited.put(name, data);
					}
				}
			}
		}
		if (duplicateData == true && formatData == true && continentData == true) {
			return true;
		} else {
			errorMessage = errorMessage.concat(formatError).concat(DuplicateError).concat(continentError);
			return false;
		}
	}

	/**
	 * This method is used to validate the list of boundary countries which are
	 * present in the uploaded file and check the correct format.
	 * 
	 * @param tagData Value of the boundary countries tag
	 * @return flag to check if entered boundary countries are valid
	 */
	public boolean validateBoundaries(String tagData) {
		HashMap<String, String> visited = new HashMap<String, String>();
		boolean duplicatedata = true, formatdata = true, adjacentdata = true, continentdata = true;
		ArrayList<String> adjacentcountries;
		String formaterror = new String(), Duplicateerror = new String(), continenterror = new String(),
				adjacencyerror = new String();
		String[] boundaryData = tagData.split("\\r\\n");
		if (boundaryData.length - 1 == listOfCountries.size()) {
			for (int i = 1; i < boundaryData.length; i++) {
				String data = boundaryData[i].trim();
				Pattern pattern = Pattern.compile("[0-9]+( [0-9]+)*");
				if (!data.trim().isEmpty()) {
					Matcher match = pattern.matcher(data.trim());
					if (!match.matches()) {
						formaterror = formaterror.concat(
								"Invalid Boundary Details. " + data.trim() + " is not defined in required format.\n");
						formatdata = false;
						continue;
					} else {
						if (visited.containsKey(data.split(" ")[0])) {
							Duplicateerror = Duplicateerror
									.concat(data.split(" ")[1] + " is already defined. Duplicate Entry Found .\n");
							duplicatedata = false;
							continue;
						} else {
							String[] countrydetail = data.split(" ");
							int countryIndex = Integer.parseInt(countrydetail[0]);
							String name = listOfCountries.get(countryIndex - 1).getCountryName();
							adjacentcountries = new ArrayList<>();
							if (!(countrydetail.length < 1)) {
								for (int j = 1; j < countrydetail.length; j++) {
									int adjcountryIndex = Integer.parseInt(countrydetail[j]);
									String adjCountryName = listOfCountries.get(adjcountryIndex - 1).getCountryName();
									adjacentcountries.add(adjCountryName);
								}
							} else {
								adjacencyerror = adjacencyerror
										.concat("No Adjacent Country defined for " + name + ".\n");
								adjacentdata = false;
								continue;
							}
							for (int k = 0; k < listOfCountries.size(); k++) {
								if (listOfCountries.get(k).getCountryName() == name) {
									listOfCountries.get(k).setNeighbourCountries(adjacentcountries);
								}
							}
							visited.put(name, data);
						}
					}
				}
			}
		} else {
			formaterror = formaterror.concat(" Invalid Number of Borders ");
			formatdata = false;
		}
		if (duplicatedata == true && formatdata == true && adjacentdata == true && continentdata == true) {
			return true;
		} else {
			errorMessage = errorMessage.concat(formaterror).concat(Duplicateerror).concat(continenterror)
					.concat(adjacencyerror);
			return false;
		}
	}

	/**
	 * 
	 * This method validates the map which is loaded and stores the details of valid
	 * data in the form of GameMap object.
	 * 
	 * @param fileName - Name of file which will be loaded.
	 * @return flag - To check if file content is valid.
	 * @throws IOException -throws for input output
	 */
	public boolean validateMap(String fileName) throws IOException {
		BufferedReader read = new BufferedReader(new FileReader(fileName));
		String fileData;
		String tagerror = new String();
		fileData = new String(Files.readAllBytes(Paths.get(fileName)));
		String[] requiredData = fileData.split("files");
		fileData = "[files" + requiredData[1];
		errorMessage = new String();

		if (!fileData.trim().isEmpty()) {
			gameMap = new GameMap();
			Pattern p = Pattern.compile("\\r\\n\\r\\n");
			String[] result = p.split(fileData);
			ArrayList<String> visitedtag = new ArrayList<String>();
			boolean invalidtag = true, validatecontinentdata = true, validatecountrydata = true,
					validateboundarydata = true, validatefilesdata = true;

			for (String tagdetails : result) {
				String tag = tagdetails.split("\\r\\n")[0].trim();
				if (tag.equalsIgnoreCase("[continents]") || tag.equalsIgnoreCase("[countries]")
						|| tag.equalsIgnoreCase("[borders]") || tag.equalsIgnoreCase("[files]")) {
					if (tag.equalsIgnoreCase("[files]")) {
						if (!visitedtag.contains(tag)) {
						} else {
							errorMessage = errorMessage.concat("Duplicate Entry for [files] Tag Found.\n");
							validatefilesdata = false;
						}
					}
					if (tag.equalsIgnoreCase("[continents]")) {
						if (!visitedtag.contains(tag)) {
							if (validateContinents(tagdetails)) {
								visitedtag.add(tag);
							} else
								validatecontinentdata = false;
						} else {
							errorMessage = errorMessage.concat("Duplicate Entry for [continents] Tag Found.\n");
							validatecontinentdata = false;
						}
					} else if (tag.equalsIgnoreCase("[countries]")) {
						if (!visitedtag.contains(tag)) {

							if (validateCountries(tagdetails)) {
								visitedtag.add(tag);
							} else
								validatecountrydata = false;
						} else {
							errorMessage = errorMessage.concat("Duplicate Entry for [countries] Tag Found.\n");
							validatecountrydata = false;
						}
					} else if (tag.equalsIgnoreCase("[borders]")) {
						if (!visitedtag.contains(tag)) {
							if (validateBoundaries(tagdetails)) {
								visitedtag.add(tag);
							} else
								validateboundarydata = false;
						} else {
							errorMessage = errorMessage.concat("Duplicate Entry for [borders] Tag Found.\n");
							validatecountrydata = false;
						}
					}
				} else {
					tagerror = tagerror.concat("Invalid " + tag + " found.\n");
					invalidtag = false;
				}
			}
			if (invalidtag == true && validatefilesdata == true && validatecontinentdata == true
					&& validatecountrydata == true && validateboundarydata == true) {
				ConnectedGraph connect = new ConnectedGraph();
				adjancencyError = connect.checkCountryAdjacency(listOfCountries, listOfContinent);
				if (adjancencyError.isEmpty()) {
					read.close();
					return true;
				} else {
					errorMessage = "Map have below error.\n";
					errorMessage = errorMessage.concat(adjancencyError);
					read.close();
					return false;
				}
			} else {
				errorMessage = tagerror.concat(errorMessage);
				errorMessage = "Map have below error\n" + errorMessage;
				read.close();
				return false;
			}
		} else {
			errorMessage = "File is Empty.\n";
			read.close();
			return false;
		}

	}
}
