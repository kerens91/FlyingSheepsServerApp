package game.gameturns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.gameplayers.Player;



public class TurnsLinkedList {
	private static final int FIRST_PLAYER_INDEX = 0;
	private static final int SEC_PLAYER_INDEX = 1;
	
	private Boolean blocked;
	private Node head;
	private Node currPlayer;
	private Map<String, Node> nodes;
	
	public TurnsLinkedList() {
		head = null;
		currPlayer = null;
		blocked = false;
		nodes = new HashMap<>();
	}
	
	
	private class Node {
		String playerId;
		Boolean isActive;
		Node next;
		Node prev;
		
		public Node (String id) {
			playerId = id;
			isActive = true;
			next = null;
			prev = null;
		}
	}
	
	
	public void addPlayers(List<Player> players) {
		/* Note: the game can have minimum 2 players */
		head = new Node(players.get(FIRST_PLAYER_INDEX).getId());
		nodes.put(head.playerId, head);
		
		Node node = head;
		for (int i = SEC_PLAYER_INDEX; i <players.size(); i++) {
			node.next = new Node(players.get(i).getId());
			node.next.prev = node;
			node = node.next;
			nodes.put(node.playerId, node);
		}
		node.next = head;
		head.prev = node;
		
		currPlayer = head;
	}
	
	public String getCurrentPlayerId() {
		return currPlayer.playerId;
	}
	
	public void setBlocked(Boolean state) {
		blocked = state;
	}
	
	public void setActive(String playerId, Boolean active) {
		Node player = nodes.get(playerId);
		player.isActive = active;
	}
		
	
	public void removePlayer(String id) {
		Node toRemove = nodes.get(id);
		toRemove.prev.next = toRemove.next;
		toRemove.next.prev = toRemove.prev;
		nodes.remove(id);
	}
	
	public Node getNextActivePlayer(Node fromPlayer) {
		Node nextPlayer = fromPlayer.next;
		while (!nextPlayer.isActive) {
			nextPlayer = nextPlayer.next;
		}
		return nextPlayer;
	}
	
	public void nextPlayerTurn() {
		Node nextActivePlayer = getNextActivePlayer(currPlayer);
		if (blocked) {
			currPlayer = getNextActivePlayer(nextActivePlayer);
			setBlocked(false);
		}
		else {
			currPlayer = nextActivePlayer;
		}
	}
}
