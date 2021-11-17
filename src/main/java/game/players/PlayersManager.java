package game.players;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import clientservershared.PlayerModel;

/**
* This class is responsible for managing the Game players.
* The class maintain a HashMap with the players as the values, mapped by their IDs.
* 
* This class suggest a set of APIs, players related, used by the Game class.
* 
* @author      Keren Solomon
*/
public class PlayersManager {
    private Map<String,Player> idToPlayersMap;
	private int numOfPlayers;
    private int numOfActivePlayers;
    
    /**
	 * Creates a PlayersManager to handle the players in the game.
	 * The players map is initiated as empty map, and number of players is initiated to 0.
	 */
    public PlayersManager() {
    	numOfPlayers = 0;
        numOfActivePlayers = 0;
        idToPlayersMap = new HashMap<>();
	}
    
    /**
	* This method registers the game manager as a callback handler to all the players events.
    * 
	*/
    public void registerPlayerNotifications() {
    	idToPlayersMap.values().forEach(player -> player.registerCallback());
	}
    
    /**
	* This method gets the number of the active players in the game.
    * 
	* @return  numOfActivePlayers    the int represents the number of active players in game.
	*/
	public int getNumOfActivePlayers() {
		return numOfActivePlayers;
	}
	
	/**
	* This method decreases the number of the active players in the game.
	* It is called when a player loses the game.
    * 
	*/
	public void decreaseNumOfActivePlayers() {
		this.numOfActivePlayers--;
	}

	/**
	* This method sets the number of players in the game.
	* It is called when a new game is created.
    * 
	* @param numOfPlayers    the int represents the number of players.
	*/
	public void setNumOfPlayers(int numOfPlayers) {
		this.numOfPlayers = numOfPlayers;
	}
	
	/**
	* This method gets a player by id.
    * 
	* @param playerId    the player id.
	* @return  Player that represents the requested player.
	*/
	public Player getPlayer(String playerId) {
		return idToPlayersMap.get(playerId);
	}
	
	/**
	* This method gets the name of a player by id.
    * 
	* @param playerId    the player id.
	* @return  String that represents the requested player's name.
	*/
	public String getPlayerName(String playerId) {
		return idToPlayersMap.get(playerId).getName();
	}
	
	/**
	* This method gets a list of the players in the game.
    * 
	* @return  List<Player> list that represents the players in the game.
	*/
	public List<Player> getPlayers() {
		return idToPlayersMap.values().stream().toList();
	}
	
	/**
	* This method gets a list of players IDs.
    * 
	* @return  List<String> list that represents the IDs of players in the game.
	*/
	public List<String> getPlayersIds() {
		return idToPlayersMap.values().stream().map(id).toList();
	}
	
	/**
	* This method gets a list of the active players in the game.
    * 
	* @return  List<Player> list that represents the active players in the game.
	*/
	public List<Player> getActivePlayers() {
		return idToPlayersMap.values()
				.stream()
				.filter(isActive)
				.collect(Collectors.toList());
    }
	
	/**
	* This method gets a list of the active players IDs.
    * 
	* @return  List<String> list that represents the IDs of the active players in the game.
	*/
	public List<String> getActivePlayersIds() {
		return idToPlayersMap.values()
				.stream()
				.filter(isActive)
				.map(id)
				.collect(Collectors.toList());
    }
	
	/**
	* This method gets a list of the active players IDs, except of the id of the given player.
	* This is used when the server needs to send an attack message to clients,
	* except of the attacker / victim client.
    * 
    * @param playerId    the player id to exclude from the list.
	* @return  List<String> list that represents the IDs of the active players in the game.
	*/
	public List<String> getActivePlayersIds(String playerId) {
		Predicate<Player> includedAndActive = isActive
				.and(player -> included.test(player, playerId));
		
		return idToPlayersMap.values()
				.stream()
				.filter(includedAndActive)
				.map(id)
				.collect(Collectors.toList());
    }
	
	/**
	* This method gets a list of the active players IDs, except of the IDs of the given players.
	* This is used when the server needs to send an attack message to clients,
	* except of both the attacker and victim clients.
    * 
    * @param playerA    the player A id to exclude from the list.
    * @param playerB    the player B id to exclude from the list.
	* @return  List<String> list that represents the IDs of the active players in the game.
	*/
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
	
	/**
	* This method adds a new player to the game list of players.
    * 
    * @param player    the new player to add.
	*/
	private void addPlayer(Player player) {
		idToPlayersMap.put(player.getId(), player);
	}
	
	/**
	* This method checks if all players joined the game.
    * 
	* @return  true|false	true if all players joined the game, otherwise false.
	*/
	public Boolean allPlayersJoined() {
		return numOfActivePlayers == numOfPlayers;
    }
	
	/**
	* This function converts a list of players to a list of PlayerModels objects.
	* The PlayerModel object is the shared object between the server and the client,
	* that contains player information.
	* 
	* This method is called by the Game class, when the game starts,
	* each player gets the list of information of all other players in the game.
	* 
	* @param id  		the string represents a player id.
	* @param current  	the string represents the id of the current player turn.
	* 
	* @return  List<PlayerModel>	the list represents the other player in the game and their information.
	*/
	public List<PlayerModel> getPlayersModels(String id, String current) {
		return idToPlayersMap.values()
				.stream()
				.filter(player -> id != player.getId())
				.map(player -> new PlayerModel(player, current))
				.collect(Collectors.toList());
	}

	/**
	* This function converts a players to a PlayerModel object.
	* The PlayerModel object is the shared object between the server and the client,
	* that contains player information.
	* 
	* This method is called by the Game class, when the game starts,
	* each player gets the relevant information to present on the game screen,
	* including name, image, score and a boolean to indicate if this is your turn to play. 
	* 
	* @param playerId  	the string represents the player id.
	* @param current  	the string represents the id of the current player turn.
	* 
	* @return  PlayerModel	that represents the player information.
	*/
	public PlayerModel getPlayerModel(String playerId, String current) {
		return new PlayerModel(idToPlayersMap.get(playerId), current);
	}
	
	/**
	* A Predicate that indicates if a player is active.
	* 
	*/
	static Predicate<Player> isActive = player -> player.isActive();
	
	/**
	* A Function that gets a player and returns it's id.
	* 
	*/
	static Function<Player,String> id = player -> player.getId();
	
	/**
	* A BiPredicate that gets a player and an id, and indicates if the player's id equals the given id.
	* 
	*/
	static BiPredicate<Player, String> included = (player, excludeId) -> player.getId() != excludeId;
	
}
