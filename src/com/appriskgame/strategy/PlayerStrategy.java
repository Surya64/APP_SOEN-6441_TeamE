package com.appriskgame.strategy;

import java.io.IOException;

import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public interface PlayerStrategy {

	public void placeArmies(GameMap gameMap, GamePlayer player) throws IOException;

	public void reinforcementPhase(GamePlayer player, GameMap gameMap) throws Exception;

	public void attackPhase(GameMap gameMap, GamePlayer player, Country attackerCountry, Country defenderCountry);

	public void fortificationPhase(GameMap gameMap, GamePlayer player) throws IOException;
}
