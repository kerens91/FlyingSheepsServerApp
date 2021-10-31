package serverConnections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import clientservershared.AttackMsg;
import clientservershared.CardModel;
import clientservershared.GameInfo;
import clientservershared.GameOver;
import eventnotifications.IClientRequestNotifications;
import globals.Configs;
import globals.Constants;



public class SocketHandler implements Runnable{
	private static final Logger logger = LogManager.getLogger(SocketHandler.class);
	Configs configs;
	
	/* Clients handling members */
	private Socket client;
	private Map<String,ClientHandler> clients;
	
	/* Server socket members */
	private ServerSocket listener;
	private ExecutorService executor;
	
	private IClientRequestNotifications gameManager;
	
	public SocketHandler()
	{
		logger.info("SOCKET HANDLER started...");
		configs = Configs.getInstance();
		clients = new HashMap<>();
		executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	@Override
	public void run() {
		logger.debug("start listening on port " + configs.getIntProperty(Constants.PORT));
		try {
			listener = new ServerSocket(configs.getIntProperty(Constants.PORT));
						
			while (true) {
				logger.debug("Server is waiting for a client connection...");
				
				client = listener.accept();
				logger.debug("Server is connected to client.\n");
				
				handleClientConnection();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	
	private void handleClientConnection() {
		String clientId = UUID.randomUUID().toString();
		ClientHandler clientThread = new ClientHandler(client, clientId, gameManager);
		clients.put(clientId, clientThread);
		executor.execute(clientThread);
	}
	
	public void registerCallback(IClientRequestNotifications gameManager) {
		this.gameManager = gameManager;
	}

	public void setClientActive(String clientId) {
		ClientHandler client = clients.get(clientId);
		client.setActive();
	}

	public void sendBroadcastNumActivePlayers(int numOfPlayers) {
		for (ClientHandler client : clients.values()) {
			client.sendNumActivePlayers(numOfPlayers);
		}
	}
	
	public void sendBroadcastStartGame() {
		for (ClientHandler client : clients.values()) {
			client.sendGameStart();
		}
	}
	
	public void sendBroadcastUpdateTurn(String currPlayerId) {
		for (ClientHandler client : clients.values()) {
				client.sendUpdateTurn(currPlayerId);
		}
	}
	
	public void sendBroadcastAddToUsedPile(CardModel card) {
		for (ClientHandler client : clients.values()) {
			client.sendCardToUsedPile(card);
		}
	}

	public void sendBroadcastGameOver(GameOver info) {
		for (ClientHandler client : clients.values()) {
			client.sendGameOver(info);
		}
	}
	
	public void sendClientGameInfo(String clientId, GameInfo gameInfo) {
		ClientHandler client = clients.get(clientId);
		client.sendGameInfo(gameInfo);
	}
	
	
	public void sendClientAddCard(String clientId, CardModel card) {
		ClientHandler client = clients.get(clientId);
		client.sendAddCard(card);
	}
	
	public void sendClientRemoveCard(String clientId, int cardId) {
		ClientHandler client = clients.get(clientId);
		client.sendRemoveCard(cardId);
	}
	
	public void sendClientUpdatePoints(String clientId, int points) {
		for (ClientHandler client : clients.values()) {
			client.sendUpdatePoints(points, clientId);
		}
	}
	
	public void sendClientGameExist(String clientId) {
		ClientHandler client = clients.get(clientId);
		client.sendGameExist();
	}

	public void sendClientGamePassword(String clientId, String password) {
		ClientHandler client = clients.get(clientId);
		client.sendGamePassword(password);
	}

	public void sendClientGameStart(String clientId, int numOfActivePlayers) {
		ClientHandler client = clients.get(clientId);
		client.sendGameStart(numOfActivePlayers);
	}

	public void sendClientPasswordInvalid(String clientId) {
		ClientHandler client = clients.get(clientId);
		client.sendPassowrdInvalid();
	}

	public void sendClientGameFull(String clientId) {
		ClientHandler client = clients.get(clientId);
		client.sendGameFull();
	}

	public void sendClientAttackMsg(String clientId, AttackMsg msg) {
		ClientHandler client = clients.get(clientId);
		client.sendAttackMsg(msg);
	}
	
	public void sendClientLostGame(String clientId) {
		ClientHandler client = clients.get(clientId);
		client.sendPlayerLostGame();
	}
	
	public void sendMultipleClientsAttackMsg(ArrayList<String> clientsIds, AttackMsg msg) {
		for (String id : clientsIds) {
			sendClientAttackMsg(id, msg);
		}
	}

}
