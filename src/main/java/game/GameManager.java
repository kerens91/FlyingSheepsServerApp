package game;

import static globals.Constants.ALL_PLAYERS_JOINED;
import static globals.Constants.DEST_ALL;
import static globals.Constants.DEST_ATTACKER;
import static globals.Constants.DEST_VICTIM;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import clientservershared.CardModel;
import clientservershared.GameInfo;
import clientservershared.GameOver;
import clientservershared.PickedCards;
import eventnotifications.IAttackNotifications;
import eventnotifications.ICardNotifications;
import eventnotifications.IClientRequestNotifications;
import eventnotifications.IGameNotifications;
import eventnotifications.IPlayerNotifications;
import game.gameattacks.AttackHandler;
import game.gameattacks.AttackMsgGenerator;
import game.gameattacks.AttackMsgWrapper;
import game.gameattacks.AttacksGenerator;
import game.gameplayers.Player;
import serverConnections.SocketHandler;

/**
* GameManager is the managing unit of the Game component,
* which coordinates requests from all other components and enables
* synchronization between them, as well as 
* the class is created once, when the application starts running,
* called by the Main class.
* The GameManager class 
* which allow an application to draw onto components realized on
* various devices or onto off-screen images.
* A Graphics object encapsulates the state information needed
* for the various rendering operations that Java supports.  This
* state information includes:
* <ul>
* <li>The Component to draw on
* <li>A translation origin for rendering and clipping coordinates
* <li>The current clip
* <li>The current color
* <li>The current font
* <li>The current logical pixel operation function (XOR or Paint)
* <li>The current XOR alternation color
*     (see <a href="#setXORMode">setXORMode</a>)
* </ul>
* <p>
* Coordinates are infinitely thin and lie between the pixels of the
* output device.
* Operations which draw the outline of a figure operate by traversing
* along the infinitely thin path with a pixel-sized pen that hangs
* down and to the right of the anchor point on the path.
* Operations which fill a figure operate by filling the interior
* of the infinitely thin path.
* Operations which render horizontal text render the ascending
* portion of the characters entirely above the baseline coordinate.
* <p>
* Some important points to consider are that drawing a figure that
* covers a given rectangle will occupy one extra row of pixels on
* the right and bottom edges compared to filling a figure that is
* bounded by that same rectangle.
* Also, drawing a horizontal line along the same y coordinate as
* the baseline of a line of text will draw the line entirely below
* the text except for any descenders.
* Both of these properties are due to the pen hanging down and to
* the right from the path that it traverses.
* <p>
* All coordinates which appear as arguments to the methods of this
* Graphics object are considered relative to the translation origin
* of this Graphics object prior to the invocation of the method.
* All rendering operations modify only pixels which lie within the
* area bounded by both the current clip of the graphics context
* and the extents of the Component used to create the Graphics object.
* 
* @author      Keren Solomon
*/
public class GameManager implements IGameNotifications, ICardNotifications, IPlayerNotifications, IClientRequestNotifications, IAttackNotifications {
	private static final Logger logger = LogManager.getLogger(GameManager.class);
	private Game game;						// represents the current running game
	private AttackHandler attackHandler;
	private AttacksGenerator attackGenerator;
	private SocketHandler socketsHandler;	// manages clients requsts and responses
	
	private GameManager() {
		logger.info("GAME MANAGER started...");
	}
	
	private static GameManager gameManagerInstance = null;
	public static GameManager getInstance() {
		if (gameManagerInstance == null) {
			gameManagerInstance = new GameManager();
		}
		return gameManagerInstance;
	}
	
	public void init() {
		logger.info("GAME MANAGER started...");
		
		game = new Game();
		
		attackHandler = game.getAttackHandler();
		attackGenerator = game.getAttackGenerator();
		
		socketsHandler = new SocketHandler();
		socketsHandler.run();
	}
	
	/**
	* This function is called once a request is made by one of the clients.
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
	* This function is event implementation,
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
	* This function is called once all players joined the game.
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
	* This function is event implementation,
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
	* This function is called once a player requested to join the game.
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
	* This function is event implementation,
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
	* This function is event implementation,
	* the event is defined in the IPlayerNotifications interface,
	* it is called by the Player class,
	* each time a player loses a card.
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
	* This function is event implementation,
	* the event is defined in the IPlayerNotifications interface,
	* it is called by the Player class,
	* each time a player have a new card in hand.
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
		CardModel card = game.getCardInfo(cardId);
		socketsHandler.sendClientAddCard(clientId, card);
	}

	/**
	* This function is event implementation,
	* the event is defined in the IPlayerNotifications interface,
	* it is called by the Player class,
	* each time a player lost/win a valuable card.
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
	* This function is event implementation,
	* the event is defined in the ICardNotifications interface,
	* it is called by the AbstractOwnerableCard class,
	* each time a player get a special card from deck.
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
	* This function is event implementation,
	* the event is defined in the IGameNotifications interface,
	* it is called by the Game class,
	* each time a player ends it's turn.
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
	* This function is event implementation,
	* the event is defined in the IGameNotifications interface,
	* it is called by the Game class,
	* each time a card used.
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
	* This function is event implementation,
	* the event is defined in the IGameNotifications interface,
	* it is called by the Game class,
	* when a player win the game.
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
	* This function is event implementation,
	* the event is defined in the IGameNotifications interface,
	* it is called by the Game class,
	* when a player loses the game.
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
	
	@Override
	public void notifyAttackOnPlayer() {
		sendAttackMsg(attackHandler.notifyDefensableAttack());
	}

	@Override
	public void askVictimForAttack() {
		sendAttackMsg(attackHandler.askVictimForAttack());
	}
	
	@Override
	public void doRockAttack() {
		attackGenerator.doRockAttack();
		attackHandler.finishAttackSuc();
	}
	
	@Override
	public void rockAttackSucceeded() {
		sendAttackMsg(attackHandler.rockAttackSucceeded());
	}
	
	@Override
	public void stealAttackSucceeded() {
		if (attackGenerator.doStealAttack()) {
			// true - special card
			game.showCoopButtonIfNeeded();
		}
		sendAttackMsg(attackHandler.stealAttackSucceeded());
	}
	
	@Override
	public void stealAttackFailed() {
		sendAttackMsg(attackHandler.stealAttackFailed());
	}
	
	@Override
	public void doRiverAttack() {
		attackGenerator.doRiverAttack();
		attackHandler.finishAttackSuc();
	}
	
	@Override
	public void riverAttackSucceeded() {
		sendAttackMsg(attackHandler.riverAttackSucceeded());
	}
	
	@Override
	public void doTreeAttack() {
		attackGenerator.doTreeAttack();
	}
	
	@Override
	public void treeAttackSucceeded() {
		sendAttackMsg(attackHandler.treeAttackSucceeded());
	}
	
	@Override
	public void treeAttackFailed() {
		sendAttackMsg(attackHandler.treeAttackFailed());
	}
	

	
	@Override
	public void natureDisasterAttackSucceeded() {
		Player victim = attackGenerator.doNatureDisasterAttack();
		sendAttackMsg(attackHandler.natureDisasterAttackSucceeded());
		game.playerLoseGame(victim);
	}
	
	@Override
	public void natureDisasterAttackFailed() {
		sendAttackMsg(attackHandler.natureDisasterAttackFailed());
	}
	
	@Override
	public void onPlayerPickedCards(String clientId, PickedCards cards) {
		game.handlePickedCards(clientId, cards);
	}
	
	@Override
	public void onAttackPlayerReq(String victimId) {
		attackGenerator.startAttackOnOtherPlayer(game.getPlayer(victimId));
	}
	
	@Override
	public void onDealCardReq(String clientId) {
		game.getCardFromDeck(clientId);
	}
	
	@Override
	public void onPlayerLostAttack(String clientId) {
		game.noCardPicked();
	}
	
	@Override
	public void attackCardPicked(AbstractCard card, Player player) {
		game.attackCardPicked(card, player);
	}

	@Override
	public void defenseCardPicked(AbstractCard card, Player player) {
		game.defenseCardPicked(card, player);
	}
	
	@Override
	public void defenseCardPickedFlyingSheep(AbstractCard card, Player player) {
		game.defenseCardFlyingSheepPicked(card, player);
	}

	@Override
	public void startNatureDisasterAttack(AbstractCard card, Player player) {
		attackGenerator.startNatureDisasterAttack(card, player);
	}

	@Override
	public void specialCoupleWinGame(Player player) {
		game.playerWinGame(player);
	}

	@Override
	public void specialCoupleShowCoopBtn() {
		// TODO: implement coop button
	}

}
