package game;

import static globals.Constants.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import attackstate.GameAttackState;
import card.AbstractCard;
import card.types.AbstractPlayableCard;
import clientservershared.CardModel;
import clientservershared.GameInfo;
import clientservershared.GameOver;
import clientservershared.GameOver.WinType;
import clientservershared.PickedCards;
import game.attacks.AttackHandler;
import game.attacks.AttackMsgGenerator;
import game.attacks.AttacksGenerator;
import game.cards.CardsManager;
import game.cards.Deck;
import game.eventnotifier.EventNotifier;
import game.players.Player;
import game.players.PlayersManager;
import game.turns.TurnsLinkedList;
import globals.Configs;

/**
* The Game class defines a single game.
* This class is responsible for creating the game, starting the game and handling main steps
* and operations in the game.
* 
* The Game class is created by the GameManager class when there is a new game request,
* generates a password as an id, in which the players will use to join the game.
* The Game class provides APIs for the GameManager use.
* The GameManager listens to events coming from Game class regarding the ongoing game.
* 
* The Game class makes use of other classes services, each of which is responsible for a different part of the game:
* <ul>
* <li>playersManager - manages the players in game
* <li>turns - keeps track of the turns
* <li>deck - handles the deck of cards
* <li>cardsManager - manages the cards in the game
* <li>attackGenerator - manages the attacks in the game
* <li>attackHandler - handles the secondary operations required as a result of the attacks
* <li>attackMsgGenerator - generates the game attack messages
* <li>gameNotifier - defines the game events
* </ul>
* 
* Once created, the Game class initiates each of the above classes as a member.
* Thus, each game has it's own set of classes, concentrated and handled by the Game class.
* 
* @author      Keren Solomon
*/
public class Game {
	private static final Logger logger = LogManager.getLogger(Game.class);
	private Configs configs;
	private Boolean isGameCreated; 	// set when the game is created
	private Boolean isGameActive; 	// set when the game starts
	private String password;		// generated when game created
	
	private PlayersManager playersManager;
    private Deck deck;
    private TurnsLinkedList turns;
    private AttackHandler attackHandler;
    private AttackMsgGenerator attackMsgGenerator;
    private AttacksGenerator attackGenerator;
    private EventNotifier gameNotifier;
    private CardsManager cardsManager;
    
    private GameOver gameOverInfo;
    
    private Boolean showCoopBtn;
	
	public Game() {
		isGameCreated = false;
        isGameActive = false;
        showCoopBtn = false;
        initMembers();
    }
	
	private void initMembers() {
		configs = Configs.getInstance();
        turns = new TurnsLinkedList();
        playersManager = new PlayersManager();
        gameNotifier = new EventNotifier();
        gameOverInfo = new GameOver();
        GameAttackState attackState = new GameAttackState();
        attackMsgGenerator = new AttackMsgGenerator(attackState);
    	attackHandler = new AttackHandler(attackState, playersManager, attackMsgGenerator, cardsManager, turns);
    	attackGenerator = new AttacksGenerator(attackHandler);
	}
    
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isGameCreated() {
    	return this.isGameCreated;
    }
    
    public Boolean isGameActive() {
    	return this.isGameActive;
    }
    
	public AttackHandler getAttackHandler() {
		return attackHandler;
	}

	public AttacksGenerator getAttackGenerator() {
		return attackGenerator;
	}
	
	public Player getPlayer(String playerId) {
		return playersManager.getPlayer(playerId);
	}

	public void createNewGame(int numPlayers) {
		logger.info("A new game is created, with " + numPlayers + " players");
    	playersManager.setNumOfPlayers(numPlayers);
    	deck = new Deck(numPlayers);
    	cardsManager = new CardsManager(deck);
    	isGameCreated = true;
    }
	
    public void startGame() {
    	logger.info("Starting game...");
    	
    	deck.initCards();
    	setPlayersHands();
    	cardsManager.addNonPlayableCardsToDeck();
    	
    	isGameActive = true;
    	
    	turns.addPlayers(playersManager.getPlayers());
    	playersManager.registerPlayerNotifications();
    	
    	logger.info("The game is starting...");
    }
    
    
    
    public GameInfo getPlayerInfo(String playerId, String currentPlayer) {
    	GameInfo info = new GameInfo();
    	
    	info.setPlayers(playersManager.getPlayersModels(playerId, currentPlayer));
    	info.setMyPlayer(playersManager.getPlayerModel(playerId, currentPlayer));
    	info.setCards(cardsManager.getCardsModels(playersManager.getPlayer(playerId).getHand()));
    	
    	return info;
    }
    
    public Map<String,GameInfo> getGameInfo() {
    	Map<String,GameInfo> playersIdToInfoMap = new HashMap<>();
    	
    	String currentPlayer = turns.getCurrentPlayerId();
    	for (String playerId : playersManager.getPlayersIds()) {
    		playersIdToInfoMap.put(playerId, getPlayerInfo(playerId, currentPlayer));
    	}
    	
    	return playersIdToInfoMap;
    }
    
    public CardModel getCardInfo(int csrdId) {
    	return cardsManager.getCardInfo(csrdId);
    }
    
	public void showCoopButtonIfNeeded() {
		if (cardsManager.isCooperationEnabled()) {
			showCoopBtn = true;
		}
	}

	/**
	* This function tries to add a new player to the game:
	* the players manager class is called to try add the player.
	* if the player successfully added, a message is printed to logger
	* and an event is sent to the game manager with the updated number
	* of active players in game.
	*
	* @param playerId  	the string to identify the player as a client connected to the server.
	* @param name  		the string represents the player name. 
	* @param img  		the string represents the player image. 
	* 
	* @return  true|false	true if the player added, otherwise false.
	*/
	public Boolean addActivePlayer(String playerId, String name, String img) {
    	if (playersManager.addActivePlayer(playerId, name, img)) {
    		logger.info("A new Player is added to the game");
    		gameNotifier.activePlayers(playersManager.getNumOfActivePlayers());
            return true;
    	}
    	return false;
    }
	
	public int allPlayersJoined() {
		return playersManager.allPlayersJoined() ? ALL_PLAYERS_JOINED : playersManager.getNumOfActivePlayers();
    }

	
    private void putInUsedcardsPile(AbstractCard card) {
    	gameNotifier.cardUsed(card.getCardInfo());
	}
	
	/*
	 * This function handles player picked two cards.
	 * There are two cases in which the card will required action:
	 *   1. In case of two identical regular cards (steal attack).
	 *   2. In case of two matching special cards.
	 * In all other cases, do nothing (just remove cards from player hand).
	 */
	public void handleCoupleCardsPicked(Player player, int card1Id, int card2Id) {
		AbstractCard c1 = deck.getCard(card1Id);
		AbstractCard c2 = deck.getCard(card2Id);
		logger.debug("handleCoupleCardsPicked: card1 " + c1.getName() + ", card2 " + c2.getName());
		putInUsedcardsPile(c2);
		
		if (c1.isPlayable() && c2.isPlayable()) {
			((AbstractPlayableCard) c1).playerPickedCouple(player, (AbstractPlayableCard) c2);
		}
		else {
			logger.error("handleCoupleCardsPicked: got a non-playable card");
		}
	}
	
	/*
	 * This function handles player picked a card.
	 * There are two cases in which the card will required action:
	 *   1. In case that a player choose to attack.
	 *   2. In case that a player choose defense.
	 * In all other cases, do nothing (just remove card from player hand).
	 */
	public void handleSingleCardPicked(Player player, int cardId) {
		AbstractCard c = deck.getCard(cardId);
		logger.debug("handleSingleCardPicked: card " + c.getName());
		putInUsedcardsPile(c);
		
		if (c.isPlayable()) {
			((AbstractPlayableCard) c).playerPickedCard(player);
		}
		else {
			logger.error("handleSingleCardPicked: got a non-playable card");
		}
	}
	
	public void handlePickedCards(String playerId, PickedCards cards) {
		Player player = playersManager.getPlayer(playerId);
		cards.handlePickedCards(
				() -> handleSingleCardPicked(player, cards.getCard1()), 
				() -> handleCoupleCardsPicked(player, cards.getCard1(), cards.getCard2()));
	}
    
	private void setPlayersHands() {
    	for (Player p : playersManager.getPlayers()) {
    		for (int i=0; i< configs.getIntProperty(HAND_NUM_CARDS) ; i++) {
    			getCardFromDeck(p);
    		}
    	}
    }
	
	public void getCardFromDeck(String playerId) {
		getCardFromDeck(playersManager.getPlayer(playerId));
    }
	
	public void getCardFromDeck(Player player) {
		switch (cardsManager.getCardFromDeck(player)) {
		case CARD_NULL:
			noMoreCardsGameOver();
			break;
		case END_TURN:
			endTurn();
			break;
		case DONE:
    		logger.info("got card from deck done");
			break;
		}
    }
    
    public void setSpecialCard(int specialCardId) {
		cardsManager.setSpecialCard(specialCardId);
		showCoopButtonIfNeeded();
	}
    
    
   
	public void defenseCardPicked(AbstractCard card, Player player) {
		if (attackHandler.isAttackActive() && attackHandler.isPlayerVictim(player)) {
			attackHandler.playerDefending(card, player, false);
		}
	}
	
	public void defenseCardFlyingSheepPicked(AbstractCard card, Player player) {
		if (attackHandler.isAttackActive() && attackHandler.isPlayerVictim(player)) {
			attackHandler.playerDefending(card, player, true);
		}
	}
	
	public void attackCardPicked(AbstractCard card, Player player) {
		if (attackHandler.isAttackActive()) {
			if (attackHandler.isPlayerVictim(player)) {
				attackHandler.victimLoseAttack();
			}
		}
		else {
			attackGenerator.startAttack(card, player);
		}
	}
	
	
	public void noCardPicked() {
		attackHandler.victimLoseAttack();
	}
	

	public void SpecialCardPicked(AbstractCard card, Player player) {
		if (attackHandler.isAttackActive() && attackHandler.isPlayerVictim(player)) {
			attackHandler.victimLoseAttack();
		}
	}
	
	public void RegularCardPicked(AbstractCard card, Player player) {
		if (attackHandler.isAttackActive() && attackHandler.isPlayerVictim(player)) {
			attackHandler.victimLoseAttack();
		}
	}
	
	public void endTurn() {
    	if (isGameActive) {
    		turns.nextPlayerTurn();
        	String currentPlayer = turns.getCurrentPlayerId();
        	gameNotifier.endTurn(currentPlayer);
    	}
    }
    
    public GameOver getGameOverInfo() {
    	return gameOverInfo;
    }
    
    private void updateGameOverInfo(Player winner, GameOver.WinType reason) {
    	logger.debug("player win game - " + winner.getName());
    	gameOverInfo.setWinner(winner);
    	gameOverInfo.setWinReason(reason);
    }
    
    public void playerWinGame(Player player) {
    	updateGameOverInfo(player, WinType.WinType_cards);
    	gameOver();
    }
    
    private void noMoreCardsGameOver() {
    	int highScore = 0;
    	Player winner = null;
    	for (Player p : playersManager.getActivePlayers()) {
			if (p.getPlayerScore() > highScore) {
				highScore = p.getPlayerScore();
				winner = p;
			}
		}
    	updateGameOverInfo(winner, GameOver.WinType.WinType_Points);
    	gameOver();
    }
    
    /*
     * This func is called after game over info is set
     * notify all players that the game is over
     */
    private void gameOver() {
    	gameNotifier.gameOver(gameOverInfo);
    }
    
    /*
     * This func is called when a player loses in a nature disaster attack
     * in case only one player is left, set player as winner
     * return boolean to indicate if game over (single player left)
     */
    public void playerLoseGame(Player victim) {
    	logger.debug("player lose game - " + victim.getName());

    	if (playersManager.getNumOfActivePlayers() == configs.getIntProperty(MIN_PLAYERS)) {
    		for (Player p : playersManager.getActivePlayers()) {
    			if (!p.getId().equals(victim.getId())) {
    				updateGameOverInfo(p, GameOver.WinType.WinType_last);
    				gameOver();
    			}
    		}
    	}
    	else {
        	turns.setPlayerNotActive(victim.getId());
        	victim.setActive(false);
        	playersManager.decreaseNumOfActivePlayers();
        	gameNotifier.lostGame(victim.getId());
    	}

    }
	
	
}
