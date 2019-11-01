package com.appriskgame.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.appriskgame.controller.Player;
import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

/**
 * This Class is used for implementation of World Domination View. Gets Notified
 * whenever there is an update to the game player
 * 
 * @author Surya
 */
public class WorldDominationView implements Observer {
	public JTextArea log = new JTextArea(200, 200);
	JFrame f = new JFrame("World Domination Map");
	String output;

	/**
	 * Method to initialize the frame.
	 */
	public void initialize() {
		f.add(log);
		log.setText(output);
		JScrollPane scroll = new JScrollPane(log);
		f.add(scroll);
		f.setSize(500, 500);
		f.setLocation(500, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		GameMap gameMap = (GameMap) o;

		if (!f.isVisible()) {
			initialize();
		}
		DecimalFormat df = new DecimalFormat("#.##");
		float totalCountries = gameMap.getCountrySet().size();
		output = "";
		for (GamePlayer player : gameMap.getPlayers()) {
			float playerCountries = player.getPlayerCountries().size();
			String mapPercent = df.format((playerCountries * 100) / totalCountries);
			List<Country> countryList = player.getPlayerCountries();
			int countryArmies = 0;
			for (Country country : countryList) {
				countryArmies = countryArmies + country.getNoOfArmies();
			}
			ArrayList<String> continentsOccupiedName = new ArrayList<String>();
			ArrayList<Continent> listOfPlayerContinents = new ArrayList<Continent>();
			Player p = new Player();
			Continent playerContinent = player.getPlayerCountries().get(0).getPartOfContinent();
			int sizeOfPlayerCountries = player.getPlayerCountries().size();
			for (int i = 0; i < sizeOfPlayerCountries; i++) {
				playerContinent = player.getPlayerCountries().get(i).getPartOfContinent();
				if (!listOfPlayerContinents.contains(playerContinent)) {
					listOfPlayerContinents.add(playerContinent);
				}
			}
			for (int i = 0; i < listOfPlayerContinents.size(); i++) {
				if (p.doesPlayerOwnAContinent(player, listOfPlayerContinents.get(i).getListOfCountries()))
					continentsOccupiedName.add(listOfPlayerContinents.get(i).getContinentName());
			}
			String continents = continentsOccupiedName.toString();
			continents = continents.substring(1, continents.length() - 1);

			output = output + "\nPlayer Name = " + player.getPlayerName() + "\nPercentage of Map Contolled = "
					+ mapPercent + "\nTotal Number of Armies = " + countryArmies + "\nContinents Controlled = "
					+ (continents.isEmpty() ? "None" : continents) + "\n";

		}
		String currentText = log.getText();
		String newLog = "*******-------*******" + " " + output;
		String appendLog = newLog + "\n" + currentText;
		log.setText(appendLog);
		f.revalidate();
		f.repaint();
	}
}
