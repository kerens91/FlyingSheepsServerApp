package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import card.types.AbstractPlayableCard;
import clientservershared.CardModel;
import clientservershared.GameInfo;
import clientservershared.GameOver;
import clientservershared.GameOver.WinType;
import clientservershared.PickedCards;
import clientservershared.PlayerModel;
import eventnotifications.IAttackNotifications;
import eventnotifications.ICardNotifications;
import eventnotifications.IGameNotifications;
import eventnotifications.IPlayerNotifications;
import game.deck.Deck;
import game.player.Player;
import game.turns.TurnsLinkedList;
import globals.Configs;
import globals.Constants;


public class Game {
	private static final Logger logger = LogManager.getLogger(Game.class);
	private Configs configs;
	private Boolean isGameCreated; 	// set when the game is created
	private Boolean isGameActive; 	// set when the game starts
	private String password;		// generated when game created
	private int numOfPlayers;		// number of players in the game (2-5)
    private int numOfActivePlayers;	// number of current active playing

    private Deck deck;				// Deck of cards
    private List<Player> players;	// list of all players in the game
    private TurnsLinkedList turns;	// linkedlist implementing players turns
    
    private GameOver gameOverInfo;
    private GameAttackHandler attackHandler;
    
    private Boolean showCoopBtn;
	private int specialCardBombID;
	private int specialCardSuperID;
	private int specialCardHusbandID;
	private int specialCardWifeID;
	
	private IGameNotifications gameNotificationsCallback;
	
	private static Game game_instance = null;
	
	private Game() {
		configs = Configs.getInstance();
        isGameCreated = false;
        isGameActive = false;
        
        initPlayersMembers();
        initCardsMembers();
    }
    
	public static Game getInstance() {
    	if (game_instance == null) {
    		game_instance = new Game();
    	}
    	return game_instance;
    }
    
    public void registerCallback (GameManager notificationsCallback) {
    	gameNotificationsCallback = (IGameNotifications)notificationsCallback;
    	attackHandler.registerCallback((IAttackNotifications)notificationsCallback);
    }
    
    private void initPlayersMembers() {
    	numOfPlayers = 0;
        numOfActivePlayers = 0;
        players = Collections.synchronizedList(new ArrayList<Player>());
        turns = new TurnsLinkedList();
    }
    
    private void initCardsMembers() {
    	gameOverInfo = new GameOver();
    	attackHandler = GameAttackHandler.getInstance();
        
        showCoopBtn = false;
        specialCardBombID = configs.getIntProperty(Constants.INVALID_CARD_ID);
        specialCardHusbandID = configs.getIntProperty(Constants.INVALID_CARD_ID);
        specialCardSuperID = configs.getIntProperty(Constants.INVALID_CARD_ID);
        specialCardWifeID = configs.getIntProperty(Constants.INVALID_CARD_ID);
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
    
    public int getNumOfPlayers() {
		return numOfPlayers;
	}

	public int getNumOfActivePlayers() {
		return numOfActivePlayers;
	}

    public void setShowCoopBtn(Boolean showCoopBtn) {
		this.showCoopBtn = showCoopBtn;
	}

    public int getSpecialCardBombID() {
		return specialCardBombID;
	}

	public void setSpecialCardBombID(int specialCardBombID) {
		this.specialCardBombID = specialCardBombID;
	}

	public int getSpecialCardSuperID() {
		return specialCardSuperID;
	}

	public void setSpecialCardSuperID(int specialCardSuperID) {
		this.specialCardSuperID = specialCardSuperID;
	}

	public int getSpecialCardHusbandID() {
		return specialCardHusbandID;
	}

	public void setSpecialCardHusbandID(int specialCardHusbandID) {
		this.specialCardHusbandID = specialCardHusbandID;
	}

	public int getSpecialCardWifeID() {
		return specialCardWifeID;
	}

	public void setSpecialCardWifeID(int specialCardWifeID) {
		this.specialCardWifeID = specialCardWifeID;
	}
	
	public void createNewGame(int numPlayers) {
		logger.info("A new game is created, with " + numPlayers + " players");
    	this.numOfPlayers = numPlayers;
    	deck = new Deck(numPlayers);
    	isGameCreated = true;
    }
	
    public void startGame(ICardNotifications gameManager) {
    	logger.info("Starting game...");
    	
    	initDeck(gameManager);
    	setPlayersHands();
    	addNonPlayableCardsToDeck();
    	
    	isGameActive = true;
    	logger.info("The game is starting...");
    	
    	startCircularTurns();
    }
    
    private void initDeck(ICardNotifications gameManager) {
    	deck.initCards(gameManager);
    	deck.shuffle();
    }
    
    private void addNonPlayableCardsToDeck() {
    	deck.addDisasterCards();
    	deck.shuffle();
    	deck.printCardsInDeck();
    }
    
    private void setPlayersHands() {
    	for (Player p : players) {
    		for (int i=0; i< configs.getIntProperty(Constants.HAND_NUM_CARDS) ; i++) {
    			getCardFromDeck(p.getId());
    		}
    	}
    }
    
    private void startCircularTurns() {
    	turns.addPlayers(players);
    	String id = turns.getCurrentPlayerId();
    	
    	logger.debug("Player " + getPlayer(id).getName() + " turn");
    }
    
    public GameInfo getPlayerInfo(String playerId) {
    	List<PlayerModel> playersModels = new ArrayList<>();
    	List<CardModel> cards = new ArrayList<>();
    	GameInfo info = new GameInfo();
    	
    	for (Player p : players) {
    		PlayerModel pm = new PlayerModel(p.getId(), p.getPlayerScore(), p.getName(), p.getImg(), p.isActive());
    		if (p.getId().equals(turns.getCurrentPlayerId())) {
				pm.setMyTurn(true);
			}
    		
    		// get my info
    		if (p.getId().equals(playerId)) {
    			info.setMyPlayer(pm);
    			for (AbstractCard c : p.getHand()) {
    				CardModel cm = getCardInfo(c.getId());
    				cards.add(cm);
    			}
    		}
    		// get other players info
    		else {
    			playersModels.add(pm);
    		}
    	}
    	
    	info.setCards(cards);
    	info.setPlayers(playersModels);
    	return info;
    }
    
    public Map<String,GameInfo> getGameInfo() {
    	Map<String,GameInfo> playersIdToInfoMap = new HashMap<>();
    	String id;
    	
    	for (Player p : players) {
    		id = p.getId();
    		playersIdToInfoMap.put(id, getPlayerInfo(id));
    	}
    	
    	return playersIdToInfoMap;
    }
    
    public CardModel getCardInfo(int cardId) {
    	AbstractCard c = deck.getCard(cardId);
    	return c.getCardInfo();
    }
    
    private void notifyGameEventActivePlayers(int numPlayers) {
    	if (gameNotificationsCallback != null) {
    		gameNotificationsCallback.onNumberOfActivePlayersChanged(numPlayers);
    	}
    	else {
    		logger.error("callback is null");
    	}
    }
    
    private void notifyGameEventCardUsed(CardModel card) {
    	if (gameNotificationsCallback != null) {
    		gameNotificationsCallback.onCardUsed(card);
    	}
    	else {
    		logger.error("callback is null");
    	}
    }
    
    private void notifyGameEventEndTurn(String currentPlayer) {
    	if (gameNotificationsCallback != null) {
    		gameNotificationsCallback.onCurrentPlayerChanged(currentPlayer);
    	}
    	else {
    		logger.error("callback is null");
    	}
    }
    
    private void notifyGameEventGameOver() {
    	if (gameNotificationsCallback != null) {
    		gameNotificationsCallback.onPlayerWinGame(gameOverInfo);
    	}
    	else {
    		logger.error("callback is null");
    	}
    }
    
    private void notifyPlayerLostGame(String playerId) {
    	if (gameNotificationsCallback != null) {
    		gameNotificationsCallback.onPlayerLostGame(playerId);
    	}
    	else {
    		logger.error("callback is null");
    	}
	}
   
    public Player getPlayer(String id) {
    	Player p = null;
    	synchronized(players)
        {
    		Iterator<Player> it = players.iterator();
  
            while (it.hasNext()) {
            	p = it.next();
            	if ( p.getId().equals(id)) {
            		return p;
            	}
            }
        }
    	return p;
    }
	
	public void showCoopButtonIfNeeded() {
		int invalidCardId = configs.getIntProperty(Constants.INVALID_CARD_ID);
		if (specialCardHusbandID != invalidCardId && specialCardWifeID != invalidCardId) {
			setShowCoopBtn(true);
		}
	}

	public Boolean addActivePlayer(String playerId, String name, String img) {
    	Boolean isPlayerAdded = false;
    	
    	if (numOfActivePlayers < numOfPlayers) {
    		logger.info("A new Player is added to the game");
    		numOfActivePlayers++;
    		isPlayerAdded = true;
        	players.add(new Player(playerId, name, img));
            notifyGameEventActivePlayers(numOfActivePlayers);
    	}
    	
    	return isPlayerAdded;
    }
	
	public Boolean allPlayersJoined() {
    	if (numOfActivePlayers == numOfPlayers) {
    		return true;
    	}
    	return false;
    }

	public void registerPlayerNotifications(IPlayerNotifications gameManager) {
    	synchronized(players)
        {
            Iterator<Player> it = players.iterator();
  
            while (it.hasNext()) {
            	it.next().registerCallback(gameManager);
            }
        }		
	}
	
    private void putInUsedcardsPile(AbstractCard card) {
		notifyGameEventCardUsed(card.getCardInfo());
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
		
		if (c1 instanceof AbstractPlayableCard && c2 instanceof AbstractPlayableCard) {
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
		
		if (c instanceof AbstractPlayableCard) {
			((AbstractPlayableCard) c).playerPickedCard(player);
		}
		else {
			logger.error("handleSingleCardPicked: got a non-playable card");
		}
	}
	
	public void handlePickedCards(String playerId, PickedCards cards) {
		int card1, card2;
		Player player = getPlayer(playerId);
		
		switch (cards.getNumOfPickedCards()) {
			case Constants.NO_PICKED_CARDS:
				logger.error("No cards picked");
				break;
			case Constants.SINGLE_PICKED_CARD:
				card1 = cards.getCard1();
				handleSingleCardPicked(player, card1);
				break;
			case Constants.COUPLE_PICKED_CARDS:
				card1 = cards.getCard1();
				card2 = cards.getCard2();
				handleCoupleCardsPicked(player, card1, card2);
				break;

			default:
				break;
		}
	}
    
    public void getCardFromDeck(String playerId) {
    	AbstractCard card = deck.getCardFromDeck();
    	if (card == null) {
    		noMoreCards();
    		gameOver();
    		return;
    	}
    	logger.debug("get card from deck: " + card.getName());
    	Player p = getPlayer(playerId);
    	if (card.playCardFromDeck(p) == Constants.CARD_END_TURN) {
    		logger.debug("got card " + card.getName() + "from deck is calling end turn");
    		endTurn();
    	}
    }
    
    
    
    public void doRockAttack() {
    	Player victim = attackHandler.getPlayerVictim();
    	AbstractCard removedCard = removePlayerCard(victim);
    	attackHandler.setHelperCard(removedCard);
    	attackHandler.nextAttackState();
    }
    
    public void doRiverAttack() {
    	blockNextPlayer();
    	attackHandler.nextAttackState();
    }
    
    public void doTreeAttack() {
    	ArrayList<String> owners = getSpecialCardsOwners();
    	attackHandler.nextAttackState();
    	if (owners.isEmpty()) {
    		attackHandler.finishAttackFail();
    	}
    	else {
    		attackHandler.finishAttackSuc();
    	}
    }
    
    public void doStealAttack() {
    	Player victim = attackHandler.getPlayerVictim();
    	Player thief = attackHandler.getPlayerAttacker();
    	AbstractCard stolenCard = stealPlayerCard(victim, thief);
    	attackHandler.setHelperCard(stolenCard);
    }
    
    public void doNatureDisasterAttack() {
    	Player victim = attackHandler.getPlayerVictim();
    	Boolean isGameOver = playerLoseGame(victim);
		if (isGameOver) {
			gameOver();
		}
    }
    
    public void startAttackOnOtherPlayer(String victimId) {
    	Player victim = getPlayer(victimId);
    	attackHandler.setVictim(victim);
    	attackHandler.executeState();
    }
    
    private Boolean isCardSpecial(int cardId) {
    	if (specialCardBombID == cardId) {
    		return true;
    	}
    	if (specialCardHusbandID == cardId) {
    		return true;
    	}
    	if (specialCardSuperID == cardId) {
    		return true;
    	}
    	if (specialCardWifeID == cardId) {
    		return true;
    	}
    	return false;
    }
    
    public AbstractCard getPlayerRandomCard(Player victim) {
    	Random rand = new Random();
    	int max = victim.getNumOfCards()-1;
    	int min = 0;
		int randomCardIndex = min + rand.nextInt(max-min);
		return victim.getCardInIndex(randomCardIndex);
    }
    
    
    public AbstractCard stealPlayerCard(Player victim, Player thief) {
    	AbstractCard c = getPlayerRandomCard(victim);
    	((AbstractPlayableCard)c).setCardUsed(victim);
    	if (isCardSpecial(c.getId())) {
    		((AbstractOwnerableCard) c).setOwners(thief.getId());
    	}
    	thief.addCardToPlayerHand(c);
    	return c;
    }
    
    public AbstractCard removePlayerCard(Player victim) {
    	AbstractCard c = getPlayerRandomCard(victim);
    	((AbstractPlayableCard)c).setCardUsed(victim);
    	return c;
    }
    
    
    public ArrayList<String> getActivePlayers() {
    	ArrayList<String> activePlayers = new ArrayList<>();
    	for (Player p : players) {
    		if (p.isActive()) {
    			activePlayers.add(p.getId());
    		}
    	}
    	return activePlayers;
    }
    
    public ArrayList<String> getActivePlayers(String playerId) {
    	ArrayList<String> activePlayers = new ArrayList<>();
    	for (Player p : players) {
    		if (p.isActive() && !(p.getId().equals(playerId))) {
    			activePlayers.add(p.getId());
    		}
    	}
    	return activePlayers;
    }
    
    public ArrayList<String> getActivePlayers(String playerAId, String playerBId) {
    	ArrayList<String> activePlayers = new ArrayList<>();
    	for (Player p : players) {
    		if (p.isActive() && !(p.getId().equals(playerAId)) && !(p.getId().equals(playerBId))) {
    			activePlayers.add(p.getId());
    		}
    	}
    	return activePlayers;
    }
    
    public ArrayList<String> getSpecialCardsOwners() {
    	ArrayList<String> toAttack = new ArrayList<>();
    	if (specialCardBombID != configs.getIntProperty(Constants.INVALID_CARD_ID)) {
    		String ownerId = ((AbstractOwnerableCard) deck.getCard(specialCardBombID)).getOwner();
    		Player owner = getPlayer(ownerId);
    		String name = owner.getName();
    		toAttack.add(name);
    	}
    	if (specialCardSuperID != configs.getIntProperty(Constants.INVALID_CARD_ID)) {
    		String ownerId = ((AbstractOwnerableCard) deck.getCard(specialCardSuperID)).getOwner();
    		Player owner = getPlayer(ownerId);
    		String name = owner.getName();
    		toAttack.add(name);
    	}
    	return toAttack;
    }
    
    public void blockNextPlayer() {
    	turns.setBlocked(true);
    }
   
	public void defenseCardPicked(AbstractCard card, Player player) {
		if (attackHandler.isAttackActive() && attackHandler.isPlayerVictim(player)) {
			playerDefending(card, player, false);
		}
	}
	
	public void defenseCardFlyingSheepPicked(AbstractCard card, Player player) {
		if (attackHandler.isAttackActive() && attackHandler.isPlayerVictim(player)) {
			playerDefending(card, player, true);
		}
	}
	
	public void attackCardPicked(AbstractCard card, Player player) {
		if (attackHandler.isAttackActive()) {
			if (attackHandler.isPlayerVictim(player)) {
				victimLoseAttack();
			}
		}
		else {
			startAttack(card, player);
		}
	}
	
	private void playerDefending(AbstractCard defenseCard, Player victim, Boolean isFlyingSheep) {
		attackHandler.setHelperCard(defenseCard);
		if (attackHandler.isVictimDefended()) {
			if (isFlyingSheep) {
				victim.removeAllExceptFlyingSheepCards(defenseCard.getId());
			}
			attackHandler.finishAttackFail();
		}
		else {
			victimLoseAttack();
		}
	}
	
	public void noCardPicked() {
		victimLoseAttack();
	}
	
	private void victimLoseAttack() {
		attackHandler.finishAttackSuc();
	}
	
	private void startAttack(AbstractCard card, Player player) {
		logger.debug("Starting attack " + card.getName() + ", attacker is " + player.getName());
		attackHandler.setAttackCard(card);
		attackHandler.setAttacker(player);
		attackHandler.executeState();
	}
	
	public void startNatureDisasterAttack(AbstractCard card, Player player) {
		attackHandler.setAttackCard(card);
		attackHandler.setVictim(player);
		attackHandler.executeState();
	}

	public void SpecialCardPicked(AbstractCard card, Player player) {
		if (attackHandler.isAttackActive() && attackHandler.isPlayerVictim(player)) {
			victimLoseAttack();
		}
	}
	
	public void RegularCardPicked(AbstractCard card, Player player) {
		if (attackHandler.isAttackActive() && attackHandler.isPlayerVictim(player)) {
			victimLoseAttack();
		}
	}
	
	public void endTurn() {
    	if (isGameActive) {
    		turns.nextPlayerTurn();
        	String currentPlayer = turns.getCurrentPlayerId();
        	
        	Player p = getPlayer(currentPlayer);
        	logger.debug("Player " + p.getName() + " Turn - " + currentPlayer);
        	
        	notifyGameEventEndTurn(currentPlayer);
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
    
    private void noMoreCards() {
    	int highScore = 0;
    	Player winner = null;
    	for (Player p : players) {
			if (p.isActive() && p.getPlayerScore() > highScore) {
				highScore = p.getPlayerScore();
				winner = p;
			}
		}
    	updateGameOverInfo(winner, GameOver.WinType.WinType_Points);
    }
    
    /*
     * This func is called after game over info is set
     * notify all players that the game is over
     */
    private void gameOver() {
    	notifyGameEventGameOver();
    }
    
    /*
     * This func is called when a player loses in a nature disaster attack
     * in case only one player is left, set player as winner
     * return boolean to indicate if game over (single player left)
     */
    public Boolean playerLoseGame(Player victim) {
    	Boolean isGameOver = false;
    	logger.debug("player lose game - " + victim.getName());

    	if (numOfActivePlayers == configs.getIntProperty(Constants.MIN_PLAYERS)) {
    		for (Player p : players) {
    			if (p.isActive() && !p.getId().equals(victim.getId())) {
    				updateGameOverInfo(p, GameOver.WinType.WinType_last);
    				isGameOver = true;
    			}
    		}
    	}
    	else {
        	turns.setActive(victim.getId(), false);
        	victim.setActive(false);
        	numOfActivePlayers--;
        	notifyPlayerLostGame(victim.getId());
    	}
    	
    	return isGameOver;
    }


	public void nextAttackState() {
		attackHandler.nextAttackState();
	}

	public void finishAttackSuc() {
		attackHandler.finishAttackSuc();
	}

}
