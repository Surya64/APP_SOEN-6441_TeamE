package com.appriskgame.view;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public class WorldDominationView implements Observer {
	public JTextArea log = new JTextArea(200, 200);
	JFrame f = new JFrame("World Domination Map");
	String output;

	public void initUI() {
		f.add(log);
		log.setText(output);
		f.setSize(500, 500);
		f.setLocation(500, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		GameMap gameMap = (GameMap) o;
		if (!f.isVisible()) {
			initUI();
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
			output = output + "\n Player Name = " + player.getPlayerName() + "\nPercentage of Map Contolled = "
					+ mapPercent + "\n Total Number of Armies = " + countryArmies + "\n";
		}
		String currentText = log.getText();
		String newLog = new Date() + " " + output;
		String appendLog = newLog + "\n" + currentText;
		log.setText(appendLog);
		f.revalidate();
		f.repaint();
	}
}
