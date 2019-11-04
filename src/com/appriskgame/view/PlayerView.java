package com.appriskgame.view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import javax.swing.JTextField;

public class PlayerView implements Observer {
	JFrame frame = new JFrame("Player View");
	String phaseName = "";
	String playerName = "";
	String mapinfo = "";
	JTextArea textInfo;
	JTextField textGamePhase;
	JTextField textPlayerName;
	JScrollPane scrollPane;

	/**
	 * Method to initialize the frame.
	 * @wbp.parser.entryPoint
	 */
	public void initialize() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocation(500, 200);
		frame.getContentPane().setLayout(null);

		JLabel lblGamePhase = new JLabel("Game Phase");
		lblGamePhase.setBounds(20, 53, 73, 25);
		frame.getContentPane().add(lblGamePhase);

		textGamePhase = new JTextField();
		textGamePhase.setEditable(false);
		textGamePhase.setBounds(128, 55, 176, 20);
		textGamePhase.setText(phaseName);
		frame.getContentPane().add(textGamePhase);
		textGamePhase.setColumns(10);

		JLabel lblPlayerName = new JLabel("Player Name");
		lblPlayerName.setBounds(20, 89, 73, 25);
		frame.getContentPane().add(lblPlayerName);

		textPlayerName = new JTextField();
		textPlayerName.setEditable(false);
		textPlayerName.setColumns(10);
		textPlayerName.setBounds(128, 91, 176, 20);
		textPlayerName.setText(playerName);
		frame.getContentPane().add(textPlayerName);

		JLabel lblNewLabel = new JLabel("Map Information :");
		lblNewLabel.setBounds(20, 145, 73, 25);
		frame.getContentPane().add(lblNewLabel);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 181, 438, 193);
		frame.getContentPane().add(scrollPane);

		textInfo = new JTextArea();
		scrollPane.setViewportView(textInfo);
		textInfo.setColumns(10);

		frame.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		GameMap gameMap = (GameMap) o;
		if (!frame.isVisible()) {
			initialize();
		}
		ArrayList<Country> print = gameMap.getCountries();
		mapinfo = "";
		for (Country country : print) {
			mapinfo = mapinfo + "\n" + country;
		}
		phaseName = gameMap.getGamePhase();
		playerName = gameMap.getCurrentPlayer();
		textGamePhase.setText(phaseName);
		textPlayerName.setText(playerName);
		textInfo.setText(mapinfo);
		frame.revalidate();
		frame.repaint();

	}

}
