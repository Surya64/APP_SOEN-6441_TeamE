package com.appriskgame.model;

import java.util.Random;
import java.util.List;

import java.util.ArrayList;

public class Dice {

	private int totalDices;
	private List<Integer> resultOfDices=new ArrayList<Integer>();
	public int getTotalDices() {
		return totalDices;
	}
	public void setTotalDices(int totalDices) {
		this.totalDices = totalDices;
	}
	public List<Integer> getResultOfDices() {
		return resultOfDices;
	}
	public void setResultOfDices(List<Integer> resultOfDices) {
		this.resultOfDices = resultOfDices;
	}
	
}
