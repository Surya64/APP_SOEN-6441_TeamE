package com.appriskgame.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.Map;

public class MapOperations {

	Map map = new Map();

	public static void main(String[] args) throws IOException {
		String inputMapName = "C:\\Users\\saich\\Desktop\\Test\\ameroki.map";
		String ouputMapName = "C:\\Users\\saich\\Desktop\\Result\\out.map";
		MapOperations loadMap = new MapOperations();
		loadMap.readMap(inputMapName);
		loadMap.writeMap(ouputMapName);

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

	public void fillCountriesInMap(String CountriesString) {
		String[] countriesList = CountriesString.split("\\r\\n");

		for (int i = 1; i < countriesList.length; i++) {
			String[] countryDetails = countriesList[i].split(" ");
			int continentNumber = Integer.parseInt(countryDetails[2]) - 1;
			Continent continent = map.getContinents().get(continentNumber);
			Country country = new Country();
			country.setCountryName(countryDetails[1]);
			country.setContinentNumber(Integer.parseInt(countryDetails[2]));
			country.setCountryNumber(Integer.parseInt(countryDetails[0]));
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
				currentCountry.getNeighbourCountries().add(neighbourCountry);
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
			String countryDetails = (i + 1) + " " + country.getCountryName() + " " + country.getContinentNumber();

			countriesDetails = countriesDetails + "\n" + countryDetails;
		}
		return countriesDetails;
	}

	public String getBoundaries() {
		String boundariesDetails = "[borders]";

		for (int i = 0; i < map.getCountries().size(); i++) {
			String boundaryDetails = i + 1 + " ";

			for (int j = 0; j < map.getCountries().get(i).getNeighbourCountries().size(); j++) {
				boundaryDetails = boundaryDetails
						+ map.getCountries().get(i).getNeighbourCountries().get(j).getCountryNumber() + " ";
			}
			boundariesDetails = boundariesDetails + "\n" + boundaryDetails;
		}
		return boundariesDetails;
	}

}
