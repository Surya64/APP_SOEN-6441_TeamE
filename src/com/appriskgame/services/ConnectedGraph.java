package com.appriskgame.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.appriskgame.model.Country;

public class ConnectedGraph {

	private HashMap<Country, Boolean> visited;
	private HashSet<Country> countryList;

	public ConnectedGraph(HashSet<Country> countryList) {
		this.countryList = countryList;
		this.visited = new HashMap<>();
		Iterator<Country> iterator = this.countryList.iterator();
		while (iterator.hasNext()) {
			visited.put(iterator.next(), false);
		}
	}

	private void depthFirstTraversal(Country startCountry) {
		visited.put(startCountry, true);
		Iterator<Country> it = startCountry.getNeighbourCountriesToAdd().iterator();
		while (it.hasNext()) {
			Country neighbourCountry = it.next();
			if (!visited.get(neighbourCountry)) {
				depthFirstTraversal(neighbourCountry);
			}
		}
	}

	public boolean isConnected() {
		depthFirstTraversal(countryList.iterator().next());
		Iterator<Country> it = countryList.iterator();
		while (it.hasNext()) {
			Country country = it.next();
			if (visited.get(country) == false) {
				System.out.println("Map is not a connected graph.");
				return false;
			}
		}
		return true;
	}

}
