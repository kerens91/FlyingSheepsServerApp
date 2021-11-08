package game.gameplayers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import clientservershared.CardModel;
import clientservershared.PlayerModel;
import eventnotifications.IPlayerNotifications;
import game.Game;
import globals.Configs;
import globals.Constants;

public class PlayersManager {
	private static final Logger logger = LogManager.getLogger(PlayersManager.class);
	private Configs configs;
    private Map<String,Player> idToPlayersMap;	// list of all players in the game
    
    static Predicate<Player> isActive = player -> player.isActive();
	static Function<Player,String> id = player -> player.getId();
	static BiPredicate<Player, String> included = (player, excludeId) -> player.getId() != excludeId;

	private int numOfPlayers;		// number of players in the game (2-5)
    private int numOfActivePlayers;	// number of current active playing
    
    public PlayersManager() {
    	configs = Configs.getInstance();
    	numOfPlayers = 0;
        numOfActivePlayers = 0;
        idToPlayersMap = new HashMap<>();
	}
    
    public void registerPlayerNotifications() {
    	idToPlayersMap.values().forEach(player -> player.registerCallback());
	}
    
    public int getNumOfPlayers() {
		return numOfPlayers;
	}

	public int getNumOfActivePlayers() {
		return numOfActivePlayers;
	}
	
	public void decreaseNumOfActivePlayers() {
		this.numOfActivePlayers--;
	}

	public void setNumOfPlayers(int numOfPlayers) {
		this.numOfPlayers = numOfPlayers;
	}
	
	public Player getPlayer(String playerId) {
		return idToPlayersMap.get(playerId);
	}
	
	public String getPlayerName(String playerId) {
		return idToPlayersMap.get(playerId).getName();
	}
	
	public List<Player> getPlayers() {
		return idToPlayersMap.values().stream().toList();
	}
	
	public List<String> getPlayersIds() {
		return idToPlayersMap.values().stream().map(id).toList();
	}
	
	public List<Player> getActivePlayers() {
		return idToPlayersMap.values()
				.stream()
				.filter(isActive)
				.collect(Collectors.toList());
    }
	
	public List<String> getActivePlayersIds() {
		return idToPlayersMap.values()
				.stream()
				.filter(isActive)
				.map(id)
				.collect(Collectors.toList());
    }
	
	public List<String> getActivePlayersIds(String playerId) {
		Predicate<Player> includedAndActive = isActive
				.and(player -> included.test(player, playerId));
		
		return idToPlayersMap.values()
				.stream()
				.filter(includedAndActive)
				.map(id)
				.collect(Collectors.toList());
    }
	
	public List<String> getActivePlayersIds(String playerA, String playerB) {
		Predicate<Player> includedAndActive = isActive
				.and(player -> included.test(player, playerA))
				.and(player -> included.test(player, playerB));
		
		return idToPlayersMap.values()
				.stream()
				.filter(includedAndActive)
				.map(id)
				.collect(Collectors.toList());
    }
	
	/**
	* This function tries to add a new player:
	* the player is added only if the number of active players is less than
	* the number of players allowed in this game.
	* in this case - add the player to the players hashmap,
	* player id as a key, and the player as a related value.
	*
	* @param playerId  	the string represents the player id.
	* @param name  		the string represents the player name. 
	* @param img  		the string represents the player image. 
	* 
	* @return  true|false	true if the player added, otherwise false.
	*/
	public Boolean addActivePlayer(String playerId, String name, String img) {
    	if (numOfActivePlayers < numOfPlayers) {
    		numOfActivePlayers++;
    		addPlayer(new Player(playerId, name, img));
    		return true;
    	}
    	return false;
    }
	
	private void addPlayer(Player player) {
		idToPlayersMap.put(player.getId(), player);
	}
	
	public Boolean allPlayersJoined() {
		return numOfActivePlayers == numOfPlayers;
    }
	
	public List<PlayerModel> getPlayersModels(String id, String current) {
		return idToPlayersMap.values()
				.stream()
				.filter(player -> id != player.getId())
				.map(player -> new PlayerModel(player, current))
				.collect(Collectors.toList());
	}

	public PlayerModel getPlayerModel(String playerId, String currentPlayer) {
		return new PlayerModel(idToPlayersMap.get(playerId), currentPlayer);
	}
	

	
}
