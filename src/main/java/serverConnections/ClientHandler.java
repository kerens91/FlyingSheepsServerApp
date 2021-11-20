package serverConnections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import clientservershared.AttackMsg;
import clientservershared.CardModel;
import clientservershared.GameInfo;
import clientservershared.GameOver;
import message.MessageCreator;
import message.MessageHandler;
import message.MsgTypeEnum;

/**
* This class represents a client connection.
* The ClientHandler class enables sending and receiving of messages between this server application and the client.
* 
* This class implements the runnable interface, thus has a thread that runs in the background,
* listens to the socket for incoming requests.
* 
* Once created, the client handler initiates the in and out buffers, in which it will use to send and receive messages.
* The class uses the services of the msgHandler class to handle the incoming requests.
* The class uses the services of the msgCreator class to generate the messages to be sent.
* 
* The class offers a set of APIs used by the socket handler class to send messages to the client.
* 
* @author      Keren Solomon
*/
public class ClientHandler implements Runnable {
	private static final Logger logger = LogManager.getLogger(ClientHandler.class);
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	private String clientId;
	private Boolean isClientActive;
	
	private MessageHandler msgHandler;
	private MessageCreator msgCreator;
	private String request;
	
	public ClientHandler(Socket clientSocket, String id) {
		this.clientId = id;
		this.socket = clientSocket;
		this.isClientActive = false;
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		msgHandler = new MessageHandler(id);
		msgCreator = new MessageCreator();
	}
	
	@Override
	public void run() {
		sendAndRecv();
	}
	
	public String getClientId() {
		return clientId;
	}
	
	private void sendAndRecv() {
		try {
		while (true) {
			
				if ((request = in.readLine()) != null) {
					logger.info("Server got msg [" + clientId + "]: " + request);
					msgHandler.handleMsg(request);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			out.close();
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void setActive() {
		isClientActive = true;
	}
	
	private void sendMsgToClient(String msg) {
		if (msg != null) {
			logger.info("sending msg [" + clientId + "]:" + msg);
			out.println(msg);
		}
	}
	
	public void sendNumActivePlayers(int numPlayers) {
		if (isClientActive) {
			String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_SET_NUM_ACTIVE_PLAYERS, numPlayers);
			sendMsgToClient(msg);
		}
	}
	
	public void sendGameStart() {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_START_GAME, clientId);
		sendMsgToClient(msg);
	}

	public void sendGameInfo(GameInfo gameInfo) {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_GAME_INFO, gameInfo);
		sendMsgToClient(msg);
	}
	
	public void sendUpdatePoints(int points, String playerId) {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_GAME_UPDATE_POINTS, points, playerId);
		sendMsgToClient(msg);
	}
	
	public void sendRemoveCard(int cardId) {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_GAME_HAND_REMOVE_CARD, cardId);
		sendMsgToClient(msg);
	}
	
	public void sendAddCard(CardModel card) {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_GAME_HAND_ADD_CARD, card);
		sendMsgToClient(msg);
	}
	
	public void sendUpdateTurn(String currPlayrId) {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_UPDATE_TURNS, currPlayrId);
		sendMsgToClient(msg);
	}

	public void sendGameExist() {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_GAME_EXIST);
		sendMsgToClient(msg);
	}

	public void sendGamePassword(String password) {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_GAME_PASS, password);
		sendMsgToClient(msg);
	}

	public void sendGameStart(int numOfActivePlayers) {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_WAIT_FOR_GAME_TO_START, numOfActivePlayers);
		sendMsgToClient(msg);
	}

	public void sendPassowrdInvalid() {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_INVALID_PASS);
		sendMsgToClient(msg);
	}

	public void sendGameFull() {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_GAME_FULL);
		sendMsgToClient(msg);
	}

	public void sendAttackMsg(AttackMsg attackMsg) {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_ATTACK_MSG, attackMsg);
		sendMsgToClient(msg);
	}

	public void sendCardToUsedPile(CardModel card) {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_CARD_USED, card);
		sendMsgToClient(msg);
	}

	public void sendGameOver(GameOver info) {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_GAME_OVER, info);
		sendMsgToClient(msg);
	}

	public void sendPlayerLostGame() {
		String msg = msgCreator.createMsg(MsgTypeEnum.SERVER_REP_LOST_GAME);
		sendMsgToClient(msg);
	}



}
