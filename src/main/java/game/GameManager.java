package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import clientservershared.AttackMsg;
import clientservershared.CardModel;
import clientservershared.GameInfo;
import clientservershared.GameOver;
import clientservershared.PickedCards;
import eventnotifications.IAttackNotifications;
import eventnotifications.ICardNotifications;
import eventnotifications.IClientRequestNotifications;
import eventnotifications.IGameNotifications;
import eventnotifications.IPlayerNotifications;
import game.player.Player;
import globals.Constants;
import serverConnections.SocketHandler;

public class GameManager implements IGameNotifications, ICardNotifications, IPlayerNotifications, IClientRequestNotifications, IAttackNotifications {
	private static final Logger logger = LogManager.getLogger(GameManager.class);
	private Game game;
	private SocketHandler socketsHandler;
	private GameAttackHandler attackHandler;
	
	public GameManager() {
		logger.info("GAME MANAGER started...");
		
		game = Game.getInstance();
		game.registerCallback(this);
		attackHandler = GameAttackHandler.getInstance();
		
		socketsHandler = new SocketHandler();
		socketsHandler.registerCallback(this);
		socketsHandler.run();
	}
	
	
	private void startNewGame() {
		logger.info("Starting the game");
		game.startGame(this);
		game.registerPlayerNotifications(this);
		
		socketsHandler.sendBroadcastStartGame();
		
		Map<String,GameInfo> playersInfo = game.getGameInfo();
		for (String id : playersInfo.keySet()) {
			socketsHandler.sendClientGameInfo(id, playersInfo.get(id));
		}

		logger.info("******Game Started******");
	}

	@Override
	public void onNumberOfActivePlayersChanged(int numOfPlayers) {
		int playersJoined = game.allPlayersJoined();
		if (playersJoined == Constants.ALL_PLAYERS_JOINED) {
			startNewGame();
		}
		else
		{
			logger.info("Sending broadcast with number of active players");
			socketsHandler.sendBroadcastNumActivePlayers(numOfPlayers);
		}
		
	}

	private void notifyAttackOnVictim() {	
		String victim = attackHandler.getVictim();
		
		socketsHandler.sendClientAttackMsg(
				victim, 
				attackHandler.notifyAttackGetVictimMsg());
		
		List<String> activePlayers = game.getActivePlayers(victim);
		socketsHandler.sendMultipleClientsAttackMsg(
				activePlayers, 
				attackHandler.notifyAttackGetAllPlayersMsg());
		
		game.nextAttackState();
	}
	

	@Override
	public void askVictimForAttack() {
		String attackerId = attackHandler.getAttacker();
		AttackMsg msg = attackHandler.preAttackGetMsg(game.getActivePlayers(attackerId));
		game.nextAttackState();
		socketsHandler.sendClientAttackMsg(attackerId, msg);
	}
	
	@Override
	public void doRockAttack() {
		game.doRockAttack();
		game.finishAttackSuc();
	}
	
	@Override
	public void rockAttackSucceeded() {
		AttackMsg msg;

		String victim = attackHandler.getVictim();
		msg = attackHandler.rockAttSucGetVictimMsg();
		socketsHandler.sendClientAttackMsg(victim, msg);
		
		List<String> activePlayers = game.getActivePlayers(victim);
		msg = attackHandler.rockAttSucGetAllPlayersMsg();
		socketsHandler.sendMultipleClientsAttackMsg(activePlayers, msg);
		
		game.nextAttackState();
	}
	
	
	@Override
	public void notifyStealAttack() {
		notifyAttackOnVictim();
	}
	
	@Override
	public void stealAttackSucceeded() {
		AttackMsg msg;
		game.doStealAttack();
		
		String victim = attackHandler.getVictim();
		msg = attackHandler.stealAttSucGetVictimMsg();
		socketsHandler.sendClientAttackMsg(victim, msg);
		
		String attacker = attackHandler.getAttacker();
		msg = attackHandler.stealAttSucGetAttMsg();
		socketsHandler.sendClientAttackMsg(attacker, msg);
				
		List<String> activePlayers = game.getActivePlayers(victim, attacker);
		msg = attackHandler.stealAttSucGetAllPlayersMsg();
		socketsHandler.sendMultipleClientsAttackMsg(activePlayers, msg);
		
		game.nextAttackState();
	}
	
	@Override
	public void stealAttackFailed() {

		socketsHandler.sendMultipleClientsAttackMsg(
				game.getActivePlayers(), 
				attackHandler.stealAttFailedGetAllPlayersMsg());
				
		game.nextAttackState();
	}
	
	@Override
	public void doRiverAttack() {
		game.doRiverAttack();
		game.finishAttackSuc();
	}
	
	@Override
	public void riverAttackSucceeded() {
		AttackMsg msg;

		String attacker = attackHandler.getAttacker();
		List<String> activePlayers = game.getActivePlayers(attacker);
		msg = attackHandler.riverAttSucGetAllPlayersMsg();
		socketsHandler.sendMultipleClientsAttackMsg(activePlayers, msg);
		
		game.nextAttackState();
	}
	
	@Override
	public void doTreeAttack() {
		game.doTreeAttack();
	}
	
	@Override
	public void treeAttackSucceeded() {
		AttackMsg msg;

		String attacker = attackHandler.getAttacker();
		msg = attackHandler.treeAttSucGetAttMsg(game.getSpecialCardsOwners());
		socketsHandler.sendClientAttackMsg(attacker, msg);
						
		List<String> activePlayers = game.getActivePlayers(attacker);
		msg = attackHandler.treeAttGetAllPlayersMsg();
		socketsHandler.sendMultipleClientsAttackMsg(activePlayers, msg);
		
		game.nextAttackState();
	}
	
	@Override
	public void treeAttackFailed() {
		AttackMsg msg;

		String attacker = attackHandler.getAttacker();
		msg = attackHandler.treeAttFailGetAttMsg();
		socketsHandler.sendClientAttackMsg(attacker, msg);
						
		List<String> activePlayers = game.getActivePlayers(attacker);
		msg = attackHandler.treeAttGetAllPlayersMsg();
		socketsHandler.sendMultipleClientsAttackMsg(activePlayers, msg);
		
		game.nextAttackState();
	}
	
	@Override
	public void notifyNatureDisasterAttack() {
		notifyAttackOnVictim();
	}
	
	
	@Override
	public void natureDisasterAttackSucceeded() {
		game.doNatureDisasterAttack();
		
		socketsHandler.sendMultipleClientsAttackMsg(
				game.getActivePlayers(), 
				attackHandler.natureAttSucGetAllPlayersMsg());
		
		game.nextAttackState();
	}
	
	@Override
	public void natureDisasterAttackFailed() {
		AttackMsg msg;

		msg = attackHandler.natureAttFailGetAllPlayersMsg();
		socketsHandler.sendMultipleClientsAttackMsg(game.getActivePlayers(), msg);
		
		game.nextAttackState();
	}
	

	@Override
	public void playerHandCardRemoved(String clientId, int cardId) {
		socketsHandler.sendClientRemoveCard(clientId, cardId);
	}

	@Override
	public void playerHandCardAdded(String clientId, int cardId) {
		CardModel card = game.getCardInfo(cardId);
		socketsHandler.sendClientAddCard(clientId, card);
	}

	@Override
	public void playerHandUpdatePoints(String clientId, int numOfPoints) {
		socketsHandler.sendClientUpdatePoints(clientId, numOfPoints);
	}

	@Override
	public void onGotFromDeckBomb(int id) {
		game.setSpecialCardBombID(id);
	}

	@Override
	public void onGotFromDeckSuper(int id) {
		game.setSpecialCardSuperID(id);
	}

	@Override
	public void onGotFromDeckHusband(int id) {
		game.setSpecialCardHusbandID(id);
		game.showCoopButtonIfNeeded();
	}

	@Override
	public void onGotFromDeckWife(int id) {
		game.setSpecialCardWifeID(id);
		game.showCoopButtonIfNeeded();
	}

	
	@Override
	public void onCurrentPlayerChanged(String currentPlayer) {
		socketsHandler.sendBroadcastUpdateTurn(currentPlayer);
	}
	
	@Override
	public void onCardUsed(CardModel card) {
		socketsHandler.sendBroadcastAddToUsedPile(card);
	}
	
	@Override
	public void onPlayerWinGame(GameOver info) {
		socketsHandler.sendBroadcastGameOver(info);
	}

	@Override
	public void onPlayerLostGame(String playerId) {
		socketsHandler.sendClientLostGame(playerId);
	}

	@Override
	public void onNewGameRequest(String clientId, int numOfPlayers) {
		// currently support only single game at a time
		if (game.isGameActive()) {
			socketsHandler.sendClientGameExist(clientId);
		}
		else {
			createNewGame(clientId, numOfPlayers);
		}
	}
	

	private void createNewGame(String clientId, int numOfPlayers) {
		game.createNewGame(numOfPlayers);
		// TODO: create password (for multiple games support)
		String password = "12345";
		game.setPassword(password);
		socketsHandler.sendClientGamePassword(clientId, password);
	}
	
	@Override
	public void onJoinGameRequest(String clientId, String password, String name, String img) {
		// currently support only single game at a time
		if (password.equals(game.getPassword())) {
			joinGame(clientId, name, img);
		}
		else {
			socketsHandler.sendClientPasswordInvalid(clientId);
		}
	}
	
	private void joinGame(String clientId, String name, String img) {
		Boolean playerAdded = game.addActivePlayer(clientId, name, img);
		if (playerAdded) {
			socketsHandler.setClientActive(clientId);
			
			// if this is the last player added - no need to send msg
			int joined = game.allPlayersJoined();
			if (joined != Constants.ALL_PLAYERS_JOINED ) {
				socketsHandler.sendClientGameStart(clientId, joined);
			}
		}
		else {
			socketsHandler.sendClientGameFull(clientId);
		}
	}
	
	@Override
	public void onPlayerPickedCards(String clientId, PickedCards cards) {
		game.handlePickedCards(clientId, cards);
	}
	
	@Override
	public void onAttackPlayerReq(String victimId) {
		game.startAttackOnOtherPlayer(victimId);
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
		game.startNatureDisasterAttack(card, player);
	}

	@Override
	public void specialCoupleWinGame(Player player) {
		game.playerWinGame(player);
	}

	@Override
	public void specialCoupleShowCoopBtn() {
		// TODO: implement coop button
	}
	public void doTest() {
		System.out.println();
	}
}
