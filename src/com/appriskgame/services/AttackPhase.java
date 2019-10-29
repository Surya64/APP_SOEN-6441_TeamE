package com.appriskgame.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;
import com.appriskgame.model.Dice;

public class AttackPhase {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public void attackPhaseControl(GamePlayer player, GameMap mapDetails) throws IOException {

	}
	
		public boolean isCountryPresent(String currentCountry, GameMap mapDetails) {
			for (int i = 0; i < mapDetails.getCountries().size(); i++) {
				if (mapDetails.getCountries().get(i).getCountryName().toString().equalsIgnoreCase(currentCountry)) {
					return true;
				}

			}
			return false;
		}

		public boolean isCountryAdjacent(Country attackCountryObject, String defenderCountry, GameMap mapDetails) {

			ArrayList<String> neighbourCountires = attackCountryObject.getNeighbourCountries();
			for (int i = 0; i < neighbourCountires.size(); i++) {
				if (neighbourCountires.get(i).equalsIgnoreCase(defenderCountry)) {
					return true;
				}
			}
			return false;
		}
		
		
		public boolean isDefenderDicePossible(int DefenderArmies, int defenderDices) {
			if (defenderDices <= 2 && defenderDices <= DefenderArmies) {
				return true;
			}
			return false;
		}
		
		
		public boolean isAttackerDicePossible(int AttackerArmies, int attackDices) {

			if (attackDices <= 3 && attackDices <= AttackerArmies - 1) {
				return true;
			}
			return false;
		}

		public boolean ableToMoveArmy(Country attackCountryObject, int moveNumberOfArmies) {

			if (moveNumberOfArmies <= 0) {
				return false;
			} else if (attackCountryObject.getNoOfArmies() - 1 >= moveNumberOfArmies) {
				return true;
			}
			return false;
		}
		
		
		public boolean isAttackerWon(Country defenderCountryObject) {
			if (defenderCountryObject.getNoOfArmies() == 0) {
				return true;
			}
			return false;
		}
		
	
	
	
	
}

	