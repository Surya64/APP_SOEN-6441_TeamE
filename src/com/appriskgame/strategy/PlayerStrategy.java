package com.appriskgame.strategy;

import java.io.IOException;
import java.util.ArrayList;

import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

public interface PlayerStrategy {

	public void placeArmies(GameMap gameMap, GamePlayer player) throws IOException;

	public void reinforcementPhase(GamePlayer player, GameMap gameMap) throws Exception;

	public void attackPhase(GameMap gameMap, GamePlayer player, ArrayList<GamePlayer> playersList) throws IOException;

	public void fortificationPhase(GameMap gameMap, GamePlayer player) throws IOException;
}
