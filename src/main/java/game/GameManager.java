package game;

import static globals.Constants.ALL_PLAYERS_JOINED;
import static globals.Constants.DEST_ALL;
import static globals.Constants.DEST_ATTACKER;
import static globals.Constants.DEST_VICTIM;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import clientservershared.CardModel;
import clientservershared.GameInfo;
import clientservershared.GameOver;
import clientservershared.PickedCards;
import database.DriverSQL;
import database.entity.CardEntity;
import eventnotifications.IAttackNotifications;
import eventnotifications.ICardNotifications;
import eventnotifications.IClientRequestNotifications;
import eventnotifications.IGameNotifications;
import eventnotifications.IPlayerNotifications;
import game.attacks.AttackMsgWrapper;
import game.players.Player;
import serverConnections.SocketHandler;

/**
* The GameManager class is the managing unit of the Game component,
* it is a singleton class, created once by the main class when the application start to run.
* 
* The GameManager is the only class in the application that access the components that runs
* sockets managing and database managing.
* The GameManager also has a Game member that runs the game.
* Only a single game is currently supported.
* 
* The game manager implements multiple interfaces:
* <ul>
* <li>IGameNotifications - game events
* <li>ICardNotifications - cards events
* <li>IPlayerNotifications - players events
* <li>IClientRequestNotifications - clients connections events
* <li>IAttackNotifications - attacks events
* </ul>
* The game manager listens to the events coming from the different components,
* and handles (or call the handling function) based on all the information.
* 
* The GameManager constitute as the linking unit for all the components in the application.
* 
* @author      Keren Solomon
*/
public class GameManager implements IGameNotifications, ICardNotifications, IPlayerNotifications, IClientRequestNotifications, IAttackNotifications {
	private static final Logger logger = LogManager.getLogger(GameManager.class);
	
	private Game game;
	private SocketHandler socketsHandler;
	private DriverSQL database;
	
	private static GameManager gameManagerInstance = null;
	
	/**
	 * Creates a Game Manager to manage the games, create socket connections and handle events.
	 * The class is created with no arguments, by the main class.
	 * This class is a singleton class, created once and accessed by other classes in the application.
	 * 
	 */
	private GameManager() {
		logger.info("GAME MANAGER started...");
	}
	
	/**
	 * This method returns a GameManager - instance of this class.
	 * If the instance member is null - create it.
	 * 
	 */
	public static GameManager getInstance() {
		if (gameManagerInstance == null) {
			gameManagerInstance = new GameManager();
		}
		return gameManagerInstance;
	}
	
	/**
	 * This method id responsible for the initiation of the main application components:
	 * <ul>
	 * <li>game - handles the game
	 * <li>database - fetch data from database
	 * <li>socketsHandler - handles socket connections
	 * </ul>
	 * 
	 */
	public void init() {
		logger.info("GAME MANAGER started...");
		
		game = new Game();
		
		database = new DriverSQL();
		database.getCardsInfoFromDb();
		
		socketsHandler = new SocketHandler();
		socketsHandler.run();
	}
	
	/**
	* This method is called once a request is made by one of the clients.
	* 
	* it is responsible for handling the request - creating a new game:
	* by calling the game member create game method.
	* 
	* For each game request, a password is generated to constitute the new game id,
	* but temporary implementation is allowing only single game at a time.
	* 
	* Then, sending a message response to the requesting client, with the generated password.
	* 
	* @param clientId  		the string represents the id of the requesting client/player.
	* @param numOfPlayers  	the int represents the number of players in the new game. 
	*/
	private void createNewGameHandler(String clientId, int numOfPlayers) {
		game.createNewGame(numOfPlayers);
		// TODO: create password (for multiple games support)
		String password = "12345";
		game.setPassword(password);
		socketsHandler.sendClientGamePassword(clientId, password);
	}
	
	/**
	* This method is event implementation,
	* the event is defined in the IClientRequestNotifications interface,
	* it is called by the MessageHandler class,
	* once a client requests to start a new game.
	* 
	* It is responsible for checking if the game not already exists,
	* and call the create new game method to handle the request.
	* 
	* @param clientId  		the string represents the id of the requesting client/player.
	* @param numOfPlayers  	the int represents the number of players in the new game. 
	*/
	@Override
	public void onNewGameRequest(String clientId, int numOfPlayers) {
		// currently support only single game at a time
		if (game.isGameActive()) {
			socketsHandler.sendClientGameExist(clientId);
		}
		else {
			createNewGameHandler(clientId, numOfPlayers);
		}
	}
	
	/**
	* This method is called once all players joined the game.
	* 
	* It is responsible for handling the request - starting the game:
	* by calling the game member start game method,
	* registering to current game events,
	* collecting all the initiated data needed per each player,
	* and sending a message to inform all related clients that the game starts.
	*/
	private void startNewGameHandler() {
		logger.info("Starting the game");
		game.startGame();
		socketsHandler.sendBroadcastStartGame();
		
		Map<String,GameInfo> playersInfo = game.getGameInfo();
		for (String id : playersInfo.keySet()) {
			socketsHandler.sendClientGameInfo(id, playersInfo.get(id));
		}

		logger.info("******Game Started******");
	}

	/**
	* This method is event implementation,
	* the event is defined in the IGameNotifications interface,
	* it is called by the Game class,
	* each time a new player is added to the current game.
	* 
	* It is responsible for updating all related clients with the new
	* number of active players in game, so they will know that a new
	* player just joined.
	* 
	* In case that all players joined the game, the game is starting.
	* 
	* @param numOfPlayers  	the int represents the number of active players in the game. 
	*/
	@Override
	public void onNumberOfActivePlayersChanged(int numOfPlayers) {
		if (ALL_PLAYERS_JOINED == game.allPlayersJoined()) {
			startNewGameHandler();
		}
		else
		{
			logger.info("Sending broadcast with number of active players");
			socketsHandler.sendBroadcastNumActivePlayers(numOfPlayers);
		}
	}
	
	/**
	* This method is called once a player requested to join the game.
	* it is responsible for handling the attempts of players to join a game:
	* try to add the player by calling the game member add active player method.
	* 
	* If succeeded, inform the clientHandler thread with it's player id,
	* and send a message response to the requesting client, saying the game is starting.
	* 
	* If failed, send a message response to the requesting client, saying the gale is already full.
	* 
	* @param clientId  	the string represents the id of the requesting client/player.
	* @param name  		the String represents the player name.
	* @param img  		the String represents the player profile image.
	*/
	private void joinGameHandler(String clientId, String name, String img) {
		if (game.addActivePlayer(clientId, name, img)) {
			socketsHandler.setClientActive(clientId);
			
			// if this is the last player added - no need to send msg
			int joined = game.allPlayersJoined();
			if (joined != ALL_PLAYERS_JOINED ) {
				socketsHandler.sendClientGameStart(clientId, joined);
			}
		}
		else {
			socketsHandler.sendClientGameFull(clientId);
		}
	}
	
	/**
	* This method is event implementation,
	* the event is defined in the IClientRequestNotifications interface,
	* it is called by the MessageHandler class,
	* each time a player wants to join the game.
	* 
	* It is responsible for checking the validation of the given password,
	* if the password matched the game, the join game method is called.
	* otherwise, inform the client that the password is invalid.
	* 
	* @param clientId  	the string represents the id of the requesting client/player.
	* @param password  	the String represents the given game password.
	* @param name  		the String represents the player name.
	* @param img  		the String represents the player profile image.
	*/
	@Override
	public void onJoinGameRequest(String clientId, String password, String name, String img) {
		// currently support only single game at a time
		if (password.equals(game.getPassword())) {
			joinGameHandler(clientId, name, img);
		}
		else {
			socketsHandler.sendClientPasswordInvalid(clientId);
		}
	}
	
	/**
	* This method is responsible for sending an attack message to a client.
	* 
	* An attack message can be sent to one of three destinations:
	* <ul>
	* <li>DEST_VICTIM - send the message to the victim of the attack.
	* <li>DEST_ATTACKER - send the message to the attacker.
	* <li>DEST_ALL - send the message to all clients (may exclude the victim / attacker).
	* </ul>
	* 
	* This method gets a list of messages to be sent, mapped by each message destination.
	* It checks the three destination options, based on the given input,
	* if not null - call the socket handler to send the message to it's destination. 
	* 
	* @param destToMsgMap  	the HashMap represents the list of messages mapped by the destination.
	*/
	private void sendAttackMsg(Map<Integer, AttackMsgWrapper> destToMsgMap) {	
		Optional.ofNullable(destToMsgMap.get(DEST_VICTIM))
			.ifPresent(attackMsgWrapper -> {
				socketsHandler.sendClientAttackMsg(attackMsgWrapper.getDestinations().get(0), attackMsgWrapper.getMsg());
			});
		
		Optional.ofNullable(destToMsgMap.get(DEST_ATTACKER))
			.ifPresent(attackMsgWrapper -> {
				socketsHandler.sendClientAttackMsg(attackMsgWrapper.getDestinations().get(0), attackMsgWrapper.getMsg());
		});
		
		Optional.ofNullable(destToMsgMap.get(DEST_ALL))
			.ifPresent(attackMsgWrapper -> {
				socketsHandler.sendMultipleClientsAttackMsg(attackMsgWrapper.getDestinations(), attackMsgWrapper.getMsg());
		});
	}
	
	/**
	* This method is responsible for fetching the cards information from database.
	* 
	* @return List<CardEntity>  	the list containing the cards entities with each card information.
	*/
	public List<CardEntity> getCardsInfo() {
		return database.getCardsInfo();
	}
	
	/**
	* This method is event implementation, the event is defined in the IPlayerNotifications interface,
	* it is called by the Player class, each time a player loses a card.
	* 
	* A player can lose it's card in one of the following cases:
	* <ul>
	* <li> used the card
	* <li> victim to a steal attack (coudn't defend himself).
	* <li> victim to a rock attack
	* </ul>
	* 
	* It is responsible for sending the client/player a message,
	* to update the screen with the cards in hand.
	* 
	* @param clientId  	the string represents the id of the requesting client/player.
	* @param cardId  	the id of the removed card.
	*/
	@Override
	public void playerHandCardRemoved(String clientId, int cardId) {
		socketsHandler.sendClientRemoveCard(clientId, cardId);
	}

	/**
	* This method is event implementation, the event is defined in the IPlayerNotifications interface,
	* it is called by the Player class, each time a player have a new card in hand.
	* 
	* A player can get a new card in one of the following cases:
	* <ul>
	* <li> get card from deck
	* <li> defeated another player in a steal attack
	* </ul>
	* 
	* It is responsible for sending the client/player a message,
	* to update the screen with the cards in hand.
	* 
	* @param clientId  	the string represents the id of the requesting client/player.
	* @param cardId  	the id of the added card.
	*/
	@Override
	public void playerHandCardAdded(String clientId, int cardId) {
		CardModel card = game.getCardsManager().getCardInfo(cardId);
		socketsHandler.sendClientAddCard(clientId, card);
	}

	/**
	* This method is event implementation, the event is defined in the IPlayerNotifications interface,
	* it is called by the Player class, each time a player lost/win a valuable card.
	* 
	* It is responsible for sending all players in the game a message,
	* to update the screen with the player's new score value.
	* 
	* @param clientId  	  the string represents the id of the requesting client/player.
	* @param numOfPoints  the updated score value.
	*/
	@Override
	public void playerHandUpdatePoints(String clientId, int numOfPoints) {
		socketsHandler.sendClientUpdatePoints(clientId, numOfPoints);
	}

	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the AbstractOwnerableCard class, each time a player get a special card from deck.
	* 
	* It is responsible for updating the game member with the new card owner.
	* 
	* @param id  the int represents the card id.
	*/
	@Override
	public void onGotFromDeckSpecial(int id) {
		game.setSpecialCard(id);
	}
	
	/**
	* This method is event implementation, the event is defined in the IGameNotifications interface,
	* it is called by the Game class, each time a player ends it's turn.
	* 
	* It is responsible for sending a message to all player in the game,
	* with the current player turn.
	* 
	* @param currentPlayer  the String represents the id of the current playing player.
	*/
	@Override
	public void onCurrentPlayerChanged(String currentPlayer) {
		socketsHandler.sendBroadcastUpdateTurn(currentPlayer);
	}
	
	/**
	* This method is event implementation, the event is defined in the IGameNotifications interface,
	* it is called by the Game class, each time a card used.
	* 
	* It is responsible for sending a message to all player in the game,
	* with the used card information,
	* so the players screen will update the 'used cards' pile.
	* 
	* @param card  the CardModel contains the used card information.
	*/
	@Override
	public void onCardUsed(CardModel card) {
		socketsHandler.sendBroadcastAddToUsedPile(card);
	}
	
	/**
	* This method is event implementation, the event is defined in the IGameNotifications interface,
	* it is called by the Game class, when a player win the game.
	* 
	* It is responsible for sending a message to all player in the game,
	* with the information about the winning and losing players,
	* so the players screen will update the game over screen.
	* 
	* @param info  the GameOver contains the information about the ended game.
	*/
	@Override
	public void onPlayerWinGame(GameOver info) {
		socketsHandler.sendBroadcastGameOver(info);
	}

	/**
	* This method is event implementation, the event is defined in the IGameNotifications interface,
	* it is called by the Game class, when a player loses the game.
	* 
	* It is responsible for sending a message to all player in the game,
	* so the players screen will mark the player as not active,
	* and the loses player screen to game over screen.
	* 
	* @param playerId  the String represents the id of the losing player.
	*/
	@Override
	public void onPlayerLostGame(String playerId) {
		socketsHandler.sendClientLostGame(playerId);
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the steal card or by one of the nature disaster cards, when an attack starts.
	* 
	* It is responsible for sending a message to all players in the game,
	* to inform them with the attack.
	*/
	@Override
	public void notifyAttackOnPlayer() {
		sendAttackMsg(game.getAttackHandler().notifyDefensableAttack());
	}

	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the pre-attack state class, when a steal / rock attack starts,
	* in order to get the victim of the attack.
	* 
	* It is responsible for sending a message to the attacker, asking him who he wants to attack.
	*/
	@Override
	public void askVictimForAttack() {
		sendAttackMsg(game.getAttackHandler().askVictimForAttack());
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the rock card, when the do-attack state is executed.
	* 
	* It is responsible for invoking the game method that handles the attack,
	* and then call finish attack success method, as the rock attack ended successfully.
	*/
	@Override
	public void doRockAttack() {
		logger.debug("doing rock attack");
		game.getAttackGenerator().doRockAttack();
		game.getAttackHandler().finishAttackSuc();
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the rock card, when the success-attack state is executed.
	* 
	* It is responsible for informing the relevant players with the attack information.
	*/
	@Override
	public void rockAttackSucceeded() {
		logger.debug("rock attack succeeded");
		sendAttackMsg(game.getAttackResolver().rockAttackSucceeded());
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the steal card, when the success-attack state is executed.
	* 
	* It is responsible for invoking the game method that handles the attack,
	* and then to inform the relevant players with the attack information.
	*/
	@Override
	public void stealAttackSucceeded() {
		logger.debug("steal attack succeeded");
		if (game.getAttackGenerator().doStealAttack()) {
			// true - special card
			game.showCoopButtonIfNeeded();
		}
		sendAttackMsg(game.getAttackResolver().stealAttackSucceeded());
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the steal card, when the failed-attack state is executed.
	* 
	* It is responsible for informing the relevant players with the attack information.
	*/
	@Override
	public void stealAttackFailed() {
		logger.debug("steal attack failed");
		sendAttackMsg(game.getAttackResolver().stealAttackFailed());
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the river card, when the do-attack state is executed.
	* 
	* It is responsible for invoking the game method that handles the attack,
	* and then call finish attack success method, as the river attack ended successfully.
	*/
	@Override
	public void doRiverAttack() {
		logger.debug("doing river attack");
		game.getAttackGenerator().doRiverAttack();
		game.getAttackHandler().finishAttackSuc();
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the river card, when the success-attack state is executed.
	* 
	* It is responsible for informing the relevant players with the attack information.
	*/
	@Override
	public void riverAttackSucceeded() {
		sendAttackMsg(game.getAttackResolver().riverAttackSucceeded());
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the tree card, when the do-attack state is executed.
	* 
	* It is responsible for invoking the game method that handles the attack,
	* and then call finish attack success method, as the tree attack ended successfully.
	*/
	@Override
	public void doTreeAttack() {
		logger.debug("doing tree attack");
		game.getAttackGenerator().doTreeAttack();
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the tree card, when the success-attack state is executed.
	* 
	* It is responsible for informing the relevant players with the attack information.
	*/
	@Override
	public void treeAttackSucceeded() {
		logger.debug("tree attack succeeded");
		sendAttackMsg(game.getAttackResolver().treeAttackSucceeded());
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the tree card, when the failed-attack state is executed.
	* 
	* It is responsible for informing the relevant players with the attack information.
	*/
	@Override
	public void treeAttackFailed() {
		logger.debug("tree attack failed");
		sendAttackMsg(game.getAttackResolver().treeAttackFailed());
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by one of the nature disaster cards, when the success-attack state is executed.
	* 
	* It is responsible for invoking the game method that handles the attack,
	* inform the relevant players with the attack information, and call the game method playerLoseGame
	* with the victim player as an argument, this will set the player as not active.
	*/
	@Override
	public void natureDisasterAttackSucceeded() {
		logger.debug("nature disaster attack succeeded");
		Player victim = game.getAttackGenerator().doNatureDisasterAttack();
		sendAttackMsg(game.getAttackResolver().natureDisasterAttackSucceeded());
		game.playerLoseGame(victim);
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by one of the nature disaster cards, when the failed-attack state is executed.
	* 
	* It is responsible for informing the relevant players with the attack information,
	* and then call the game method end turn.
	*/
	@Override
	public void natureDisasterAttackFailed() {
		logger.debug("nature disaster attack failed");
		sendAttackMsg(game.getAttackResolver().natureDisasterAttackFailed());
		game.endTurn();
	}
	
	/**
	* This method is event implementation, the event is defined in the IClientRequestNotifications interface,
	* it is called by the Message Handler class, that handles the clients requests.
	* 
	* In this case, one of the players in the game requested to play one or two of his hand cards.
	* It is responsible for invoking the game method to handle the picked cards.
	* 
	* @param clientId  the String represents the id of the requesting client.
	* @param cards     the PickedCards object contains the information about the picked cards.
	*/
	@Override
	public void onPlayerPickedCards(String clientId, PickedCards cards) {
		game.handlePickedCards(clientId, cards);
	}
	
	/**
	* This method is event implementation, the event is defined in the IClientRequestNotifications interface,
	* it is called by the Message Handler class, that handles the clients requests.
	* 
	* In this case, one of the players in the game requested to attack another player.
	* It is responsible for invoking the game method to handle the request and start the attack on another player.
	* 
	* @param victimId  the String represents the id of the player to attack.
	*/
	@Override
	public void onAttackPlayerReq(String victimId) {
		game.getAttackGenerator().startAttackOnOtherPlayer(game.getPlayersManager().getPlayer(victimId));
	}
	
	/**
	* This method is event implementation, the event is defined in the IClientRequestNotifications interface,
	* it is called by the Message Handler class, that handles the clients requests.
	* 
	* In this case, one of the players in the game requested to get a new card from the deck.
	* It is responsible for invoking the game method to handle the request and pull a card from the deck and add it to
	* the player's hand.
	* 
	* @param clientId  the String represents the requesting client.
	*/
	@Override
	public void onDealCardReq(String clientId) {
		game.getCardFromDeck(clientId);
	}
	
	/**
	* This method is event implementation, the event is defined in the IClientRequestNotifications interface,
	* it is called by the Message Handler class, that handles the clients requests.
	* 
	* In this case, one of the players in the game, which was under attack, hadn't managed to defend himself during
	* the 10 seconds - thus losing the attack.
	* It is responsible for invoking the game method to handle the case.
	* 
	* @param clientId  the String represents the losing client.
	*/
	@Override
	public void onPlayerLostAttack(String clientId) {
		game.getAttackHandler().noDefenseCardPicked();
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the card class (which is of type attack card) when a player chose to play this card.
	* 
	* It is responsible for invoking the game method to handle the picked card case.
	* 
	* @param card    the AbstractCard represents the picked attack card.
	* @param player  the Player that picked the card.
	*/
	@Override
	public void attackCardPicked(AbstractCard card, Player player) {
		game.attackCardPicked(card, player);
	}

	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the dog card or by the stick card, when a player chose to play this card.
	* 
	* It is responsible for invoking the game method to handle the picked card case.
	* 
	* @param card    the AbstractCard represents the picked defense card.
	* @param player  the Player that picked the card.
	*/
	@Override
	public void defenseCardPicked(AbstractCard card, Player player) {
		game.getAttackHandler().defenseCardPicked(card, player);
	}
	
	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the flying sheep card, when a player chose to play this card.
	* 
	* It is responsible for invoking the game method to handle the picked card case.
	* 
	* @param card    the AbstractCard represents the flying sheep picked defense card.
	* @param player  the Player that picked the card.
	*/
	@Override
	public void defenseCardPickedFlyingSheep(AbstractCard card, Player player) {
		game.getAttackHandler().defenseCardFlyingSheepPicked(card, player);
	}

	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by one of the nature disaster cards, when pulled from the deck.
	* 
	* It is responsible for invoking the game method to start the attack.
	* 
	* @param card    the AbstractCard represents the nature disaster attack card.
	* @param player  the Player that got the card from deck.
	*/
	@Override
	public void startNatureDisasterAttack(AbstractCard card, Player player) {
		game.getAttackGenerator().startNatureDisasterAttack(card, player);
	}

	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the super flying card or the nuclear bomb card, when one of the players used the
	* couple - super flying sheep card and the nuclear bomb card, which picking both cards gives the player victory.
	* 
	* It is responsible for invoking the game method to end the game with the player as the winner.
	* 
	* @param player  the Player that picked the cards.
	*/
	@Override
	public void specialCoupleWinGame(Player player) {
		game.playerWinGame(player);
	}

	/**
	* This method is event implementation, the event is defined in the ICardNotifications interface,
	* it is called by the flying husband card or the flying wife card, when one of the players chose to play the card.
	* 
	* It is responsible for invoking a game method to handle the case of cooperation between two players.
	* This is not yet implemented.
	*/
	@Override
	public void specialCoupleShowCoopBtn() {
		// TODO: implement coop button
	}

}
