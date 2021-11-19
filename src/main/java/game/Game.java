package game;

import static globals.Constants.*;
import clientservershared.GameOver.WinType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import attackstate.GameAttackState;
import card.AbstractCard;
import card.types.AbstractPlayableCard;
import clientservershared.GameInfo;
import clientservershared.GameOver;
import clientservershared.PickedCards;
import game.attacks.AttackHandler;
import game.attacks.AttackMsgGenerator;
import game.attacks.AttackResolver;
import game.attacks.AttacksGenerator;
import game.cards.CardsManager;
import game.cards.Deck;
import game.eventnotifier.EventNotifier;
import game.players.Player;
import game.players.PlayersManager;
import game.turns.TurnsLinkedList;
import globals.Configs;

/**
* The Game class represents a single game.
* The game class defines the rules and the steps required for the game to run fluently.
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
* <li>attackResolver - manages the attack result and finishes the attack
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
    private AttackResolver attackResolver;
    private EventNotifier gameNotifier;
    private CardsManager cardsManager;
    
    private GameOver gameOverInfo;
    
    private Boolean showCoopBtn;
	
    /**
	 * Creates a Game to represent the form of play in the application.
	 * The class is created with no arguments, by the game manager class.
	 * 
	 * The game is initiated with the 'is created' field as false, which will be set to true once a new game
	 * request is made by one of the clients.
	 * 
	 * The game is initiated as 'no active' status, which will be set to active once all players joined
	 * and the game starts.
	 * 
	 * The game class calls a method to initiate all members classes that will supply services to this class during the game.
	 */
	public Game() {
		isGameCreated = false;
        isGameActive = false;
        showCoopBtn = false;
        initMembers();
    }
	
	/**
	* This method is responsible for the initiation of a set of classes that
	* supplies services to this class during the game.
	* 
	*/
	private void initMembers() {
		configs = Configs.getInstance();
        turns = new TurnsLinkedList();
        playersManager = new PlayersManager();
        gameNotifier = new EventNotifier();
        gameOverInfo = new GameOver();
        GameAttackState attackState = new GameAttackState();
        attackMsgGenerator = new AttackMsgGenerator(attackState);
    	attackHandler = new AttackHandler(attackState, playersManager, attackMsgGenerator, turns);
    	attackGenerator = new AttacksGenerator(attackHandler);
    	attackResolver = new AttackResolver(attackHandler);
	}
    
	/**
	* This method is a getter for the game password.
	* @return  password 	the string represents the game password.
	*/
    public String getPassword() {
		return password;
	}

    /**
	* This method is a setter for the game password.
	* @param password    	the string represents the game password.
	*/
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	* This method is a getter for the game creation status.
	* @return  true|false	true if the game created, otherwise false.
	*/
	public Boolean isGameCreated() {
    	return isGameCreated;
    }
    
	/**
	* This method is a getter for the game status.
	* @return true|false	true if the game is active, otherwise false.
	*/
    public Boolean isGameActive() {
    	return isGameActive;
    }
    
    /**
	* This method is a getter for the game-over information object.
	* @return GameOver the objects contains the details required for ending the game.
	*/
    public GameOver getGameOverInfo() {
    	return gameOverInfo;
    }
    
    /**
	* This method is a getter for the game AttackHandler object.
	* @return AttackHandler the objects represents the game attack handler member.
	*/
	public AttackHandler getAttackHandler() {
		return attackHandler;
	}
	
	/**
	* This method is a getter for the game AttackResolver object.
	* @return AttackResolver the objects represents the game attack resolver member.
	*/
	public AttackResolver getAttackResolver() {
		return attackResolver;
	}

	/**
	* This method is a getter for the game AttacksGenerator object.
	* @return AttacksGenerator the objects represents the game attack generator member.
	*/
	public AttacksGenerator getAttackGenerator() {
		return attackGenerator;
	}
	
	/**
	* This method is a getter for the game PlayersManager object.
	* @return PlayersManager the objects represents the game players manager member.
	*/
	public PlayersManager getPlayersManager() {
		return playersManager;
	}

	/**
	* This method is a getter for the game CardsManager object.
	* @return CardsManager the objects represents the game cards manager member.
	*/
	public CardsManager getCardsManager() {
		return cardsManager;
	}

	/**
	* This method is responsible for creation of a new game.
	* The method sets the number of players, initiates the cards classes, and sets the game as created.
	* 
	* @param numPlayers    	the int represents the number of players in the game.
	*/
	public void createNewGame(int numPlayers) {
		logger.info("A new game is created, with " + numPlayers + " players");
    	playersManager.setNumOfPlayers(numPlayers);
    	deck = new Deck(numPlayers);
    	cardsManager = new CardsManager(deck);
    	attackHandler.setCardsManager(cardsManager);
    	isGameCreated = true;
    }
	
	/**
	* This method is responsible for starting the game.
	* The method sets the number of players, initiates the cards, deals cards to each player,
	* registers players event callback, and sets the game as active.
	* 
	*/
    public void startGame() {
    	logger.debug("Starting game...");
    	
    	deck.initCards();
    	setPlayersHands();
    	cardsManager.addNonPlayableCardsToDeck();
    	
    	isGameActive = true;
    	
    	turns.addPlayers(playersManager.getPlayers());
    	playersManager.registerPlayerNotifications();
    	
    	logger.info("The game is starting...");
    }
    
    
    /**
	* This method is responsible for collecting the necessary data needed for a player,
	* including the player's details, the details of the other players in the game, and
	* the player's hand of cards.
	* 
	* It is called when the game starts.
    * 
	* @param playerId    	the string represents the player's id.
	* @param currentPlayer  the string represents the id of the current playing player.
	* @return  GameInfo 	the GameInfo object that represents the player's information.
	*/
    public GameInfo getPlayerInfo(String playerId, String currentPlayer) {
    	GameInfo info = new GameInfo();
    	
    	info.setPlayers(playersManager.getPlayersModels(playerId, currentPlayer));
    	info.setMyPlayer(playersManager.getPlayerModel(playerId, currentPlayer));
    	info.setCards(cardsManager.getCardsModels(playersManager.getPlayer(playerId).getHand()));
    	
    	return info;
    }
    
    /**
	* This method is responsible for collecting the necessary data needed for the game,
	* including each player's information and general game information.
	* 
	* It is called by the game manager when the game starts.
	* It generates a list of GameInfo objects mapped by the player's id, so that the socket
	* manager can send each GameInfo object with the relevant information to the specific client.
    * 
	* @return  Map<String,GameInfo> a HashMap containing the players IDs and their GameInfo information.
	*/
    public Map<String,GameInfo> getGameInfo() {
    	Map<String,GameInfo> playersIdToInfoMap = new HashMap<>();
    	
    	String currentPlayer = turns.getCurrentPlayerId();
    	for (String playerId : playersManager.getPlayersIds()) {
    		playersIdToInfoMap.put(playerId, getPlayerInfo(playerId, currentPlayer));
    	}
    	
    	return playersIdToInfoMap;
    }
    
    /**
	* This method decides if the cooperation button is needed.
	* It is called when a special card changes it's owners.
	* 
	*/
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
	
	/**
	* This method checks if all players joined the game.
	* It is called when a special card changes it's owners.
	* 
	* @return ALL_PLAYERS_JOINED | int   ALL_PLAYERS_JOINED if all players joined, otherwise the number of active players.
	*/
	public int allPlayersJoined() {
		return playersManager.allPlayersJoined() ? ALL_PLAYERS_JOINED : playersManager.getNumOfActivePlayers();
    }

	/**
	* This method is responsible for marking a card as used.
	* It is called when a player used the card.
	* 
	* @param card  the AbstractCard represents the used card.
	*/
    private void putInUsedcardsPile(AbstractCard card) {
    	gameNotifier.cardUsed(card.getCardInfo());
	}
	
    /**
	* This method handles player picked two cards.
	* There are two cases in which the card will required action:
	* <ul>
	* <li>In case of two identical regular cards (steal attack).
	* <li>In case of two matching special cards.
	* </ul>
	* In all other cases, do nothing (just remove cards from player hand).
	*
	* @param player  	the player that picked the cards.
	* @param card1Id  	the int represents the first card id.
	* @param card2Id  	the int represents the second card id.
	* 
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
	
	/**
	* This method handles player picked a card.
	* There are two cases in which the card will required action:
	* <ul>
	* <li>In case that a player choose to attack.
	* <li>In case that a player choose defense.
	* </ul>
	* In all other cases, do nothing (just remove card from player hand).
	*
	* @param player  	the player that picked the card.
	* @param cardId  	the int represents the card id.
	* 
	*/
	public void handleSingleCardPicked(Player player, int cardId) {
		AbstractCard c = deck.getCard(cardId);
		logger.debug("handleSingleCardPicked: card " + c.getName());
		putInUsedcardsPile(c);
		
		if (c.isPlayable()) ((AbstractPlayableCard) c).playerPickedCard(player);
		else logger.error("handleSingleCardPicked: got a non-playable card");
	}
	
	/**
	* This method handles player picked cards.
	* It checks if a single card was picked, or a couple, and handles accordingly.
	* 
	* This is called by the game manager when a player picked cards.
	*
	* @param playerId  	the string represents that player's id.
	* @param cards  	the PickedCards object represents the picked cards.
	*/
	public void handlePickedCards(String playerId, PickedCards cards) {
		Player player = playersManager.getPlayer(playerId);
		cards.handlePickedCards(
				() -> handleSingleCardPicked(player, cards.getCard1()), 
				() -> handleCoupleCardsPicked(player, cards.getCard1(), cards.getCard2()));
	}
    
	/**
	* This method is responsible for creating a 'hand' of five cards for each player.
	* This is called when the game starts, each player starts with five random cards in his hand.
	* 
	* Note that the cards are pulled from the deck, but at this point of the game the players are
	* not yet registered to the event callback, therefore the cards can be pulled with no extra action required.
	* 
	*/
	private void setPlayersHands() {
		playersManager.getPlayers().stream()
			.forEach(player -> 
			{
				logger.debug("Initiating hand for " + player.getName() + ":");
				IntStream.range(0, configs.getIntProperty(HAND_NUM_CARDS)).forEach(i -> createHand.accept(player));
			});
    }
	
	/**
	* A Consumer that gets a player, pulls a new card from deck and put it in the given player's hand.
	* 
	*/
	public Consumer<Player> createHand = player -> getCardFromDeck(player);
	
	/**
	* This method is handling a player request to get a new card from the deck.
	* This is called by the game manager with the player's id, gets the player and calls the
	* getCardFromDeck method with the actual player.
	* 
	* @param playerId  	the string represents the id of the player that requested to get a card from the deck.
	*/
	public void getCardFromDeck(String playerId) {
		getCardFromDeck(playersManager.getPlayer(playerId));
    }
	
	/**
	* This method is handling a player request to get a new card from the deck.
	* This is called in each player's turn, as the turn ends with the player pulling a card from deck.
	* 
	* There are three cases in which pulling a card from the deck will required action:
	* <ul>
	* <li>In case that there are no more cards in the deck - end game.
	* <li>In case that the pulled card is a nature disaster card - start attack.
	* </ul>
	* otherwise just end the turn.
	* 
	* @param player  	the player that requested to get a card from the deck.
	*/
	public void getCardFromDeck(Player player) {
		switch (cardsManager.getCardFromDeck(player)) {
		case CARD_NULL:
			noMoreCardsGameOver();
			break;
		case END_TURN:
			endTurn();
			break;
		case DONE:
    		logger.debug("got card from deck done, attack started");
			break;
		}
    }
    
	/**
	* This method is handling the case in which a special card was pulled from the deck.
	* It sets the card's owner and check if cooperation button is needed.
	* 
	* @param specialCardId  the string represents the id of the pulled special card.
	*/
    public void setSpecialCard(int specialCardId) {
    	logger.debug("special card pulled from deck");
		cardsManager.setSpecialCard(specialCardId);
		showCoopButtonIfNeeded();
	}
    
    /**
	* This method is handling the case in which an attack card was picked by a player.
	* It calls the attack generator to start the attack.
	* 
	* @param card  	 the AbstractCard represents the attack card.
	* @param player  the Player that picked the card.
	*/
    public void attackCardPicked(AbstractCard card, Player player) {
    	logger.debug(player.getName() + " picked attack card");
		if (attackHandler.attackCardPicked(card, player)) attackGenerator.startAttack(card, player);
	}
	
    /**
	* This method is handling the case in which a player finishes it's turn,
	* that happens after the player pulled a card from deck.
	* 
	* It sets the next player turn and notify all players in the game.
	*/
	public void endTurn() {
    	if (isGameActive) {
    		turns.nextPlayerTurn();
        	String currentPlayer = turns.getCurrentPlayerId();
        	gameNotifier.endTurn(currentPlayer);
    	}
    }
    
	/**
	* This method is handling the case in which the game is over (if there are no more cards
	* or if a player win a game).
	* It sets the relevant information about the game - winner and the winning reason.
	* 
	* @param winner  the winning player.
	* @param reason  the winning type.
	*/
    private void updateGameOverInfo(Player winner, GameOver.WinType reason) {
    	        logger.debug("player win game - " + winner.getName());
    	        gameOverInfo.setWinner(winner);
    	        gameOverInfo.setWinReason(reason);
    }
    
	/**
	* This method is handling the case in which a player used the winning cards - couple special
	* cards (super flying sheep and the nuclear bomb).
	* It updates the GameOverInfo object with the winning details and finishes the game.
	* 
	* @param player  the winning player.
	*/
    public void playerWinGame(Player player) {
    	updateGameOverInfo(player, WinType.WinType_cards);
    	gameOver();
    }
    
    /**
	* This method is handling the case in which there are no more cards in deck.
	* In this case the game is over.
	* 
	* It checks among the active players who has the highest score in game - sets this
	* player as the winner.
	* 
	*/
    private void noMoreCardsGameOver() {
    	Player winner = playersManager.getActivePlayers().stream()
    			.sorted(Comparator.comparing(Player::getPlayerScore).reversed())
    			.collect(Collectors.toList())
    			.get(0);
    	
    	updateGameOverInfo(winner, GameOver.WinType.WinType_Points);
    	gameOver();
    }
    
    /**
	* This method is handling the case in which the game is over.
	* This is called after game over info is set.
	* 
	* It triggers an event to notify all players that the game is over with the information.
	*/
    private void gameOver() {
    	gameNotifier.gameOver(gameOverInfo);
    }
    
    /**
	* This method is handling the case in which a player lost the game.
	* This is called after the player loses the attack.
	* 
	* It sets the player as not active and notifies all other players.
	* 
	* @param victim  the losing player.
	*/
    private void playerLostGame(Player victim) {
    	turns.setPlayerNotActive(victim.getId());
    	victim.setActive(false);
    	playersManager.decreaseNumOfActivePlayers();
    	gameNotifier.lostGame(victim.getId());
    }
    
    /**
	* This method is handling the case in which a player lost the game, and only
	* one player is left, so he is automatically the winner.
	* 
	* It finds and sets the winning player calls the game over method.
	* 
	* @param victim  the losing player.
	*/
    private void endGame(Player victim) {
    	Player winner = playersManager.getActivePlayers().stream()
    			.filter(player -> !player.getId().equals(victim.getId()))
    			.findAny()
    			.orElse(null);
    		
    		Optional.ofNullable(winner)
    			.ifPresentOrElse(
    					(winPlayer) -> {
    						updateGameOverInfo(winPlayer, GameOver.WinType.WinType_last);
    						gameOver();
    					},
    					() -> logger.error("Did not find a match to the winner player"));
    }
    
    /**
	* This method is handling the case in which a player lost the game, and needs
	* to check if there are enough players left as active in the game.
	* 
	* In case only one player left - call the end game method,
	* otherwise call the player lost attack method.
	* 
	* @param victim  the losing player.
	*/
    public void playerLoseGame(Player victim) {
    	logger.debug("player lose game - " + victim.getName());

    	if (minPlayers.get()) endGame(victim);
    	else playerLostGame(victim);
    }
    
    /**
	* A Supplier that returns a boolean to indicate if the number of active players is the minimum number of players.
	* 
	*/
    private Supplier<Boolean> minPlayers = () -> playersManager.getNumOfActivePlayers() == configs.getIntProperty(MIN_PLAYERS);
	
	
}
