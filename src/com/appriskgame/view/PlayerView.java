package com.appriskgame.view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PlayerView implements Observer {
	public JTextArea log = new JTextArea(200, 200);
	JFrame f = new JFrame("Player View");
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

		if (!f.isVisible()) {
			initialize();
		}
		String currentText = log.getText();
		String newLog = "*******-------*******" + " " + output;
		String appendLog = newLog + "\n" + currentText;
		log.setText(appendLog);
		f.revalidate();
		f.repaint();

	}

}
