package message;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import clientservershared.PickedCards;
import eventnotifications.IClientRequestNotifications;
import game.GameManager;



public class MessageHandler {
	private static final Logger logger = LogManager.getLogger(MessageHandler.class);
	private MessageConvertor convertor;
	private IClientRequestNotifications clientNotifications;
	private String clientId;
	
	public MessageHandler(String id) {
		convertor = new MessageConvertor();
		clientId = id;
		clientNotifications = GameManager.getInstance();
	}
	
	public void handleMsg(String jsomMsg) {
		Message msg = convertor.jsonToMsg(jsomMsg);
		
		switch (msg.getMsgType()) {
		case CLIENT_REQ_NEW_GAME:
			logger.debug("player requested to start a new game");
			handleNewGameReq(msg.getMsgParams());
			break;
			
		case CLIENT_REQ_JOIN_GAME:
			logger.debug("player requested to join a game game");
			handleJoinGameReq(msg.getMsgParams());
			break;
			
		case CLIENT_REQ_PICKED_CARDS:
			logger.debug("player requested to pick cards");
			handlePickedCardsReq(msg.getMsgParams());
			break;
			
		case CLIENT_REQ_DEAL_CARD:
			logger.debug("player requested to deal a card from deck");
			handleDealCardReq();
			break;
			
		case CLIENT_REQ_ATTACK_REP_VICTIM:
			logger.debug("player replyed with id of player to attack");
			handleAttackPlayerReq(msg.getMsgParams());
			break;
			
		case CLIENT_REQ_LOSE_ATTACK:
			logger.debug("player replyed with lost attack msg");
			handleLostAttackReq();
			break;

		default:
			logger.debug("player requested other type of message");
			break;
		}
	}
	
	private void handleNewGameReq(ArrayList<Object> msgParams) {
		Integer numPlayers = ((Double)msgParams.get(0)).intValue();
		clientNotifications.onNewGameRequest(clientId, numPlayers);
	}
	
	private void handleJoinGameReq(ArrayList<Object> msgParams) {
		String password = (String) msgParams.get(0);
		String name = (String) msgParams.get(1);
		String img = (String) msgParams.get(2);
		clientNotifications.onJoinGameRequest(clientId, password, name, img);
	}
	
	private void handlePickedCardsReq(ArrayList<Object> msgParams) {
		String jsonInfo = (String) msgParams.get(0);
		PickedCards cards = convertor.jsonToPickedCards(jsonInfo);
		clientNotifications.onPlayerPickedCards(clientId, cards);
	}
	
	private void handleDealCardReq() {
		clientNotifications.onDealCardReq(clientId);
	}
	
	private void handleAttackPlayerReq(ArrayList<Object> msgParams) {
		String victimId = (String) msgParams.get(0);
		clientNotifications.onAttackPlayerReq(victimId);
	}
	
	private void handleLostAttackReq() {
		clientNotifications.onPlayerLostAttack(clientId);
	}
	
	
}
