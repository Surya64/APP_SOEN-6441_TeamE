package com.appriskgame.strategy;

import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public interface PlayerStrategy {

	public void placeArmies(GameMap gameMap, GamePlayer player, Country country);

	public void reinforcementPhase(GamePlayer player, GameMap gameMap, Country country, int reinforceArmyCount);

	public void attackPhase(GameMap gameMap, GamePlayer player, Country attackerCountry, Country defenderCountry);

	public void fortificationPhase(GameMap gameMap, GamePlayer player, Country fromCountry, Country toCountry,
			int armiesCount);
}
