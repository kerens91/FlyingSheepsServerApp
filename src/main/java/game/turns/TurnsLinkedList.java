package game.turns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.players.Player;

/**
* This class is responsible for handling players turns.
* This class is a doubly linked list implementation,
* where each item in the list contains a player identifier and a status (active / not active).
* 
* The Node class is a private class defined in the TurnsLinkedList,
* each node is an item in the list, represents a player in the game.
* 
* The class has a member currPlayer which holds, during the game, the current player turn.
* The class also have a boolean member blocked, which indicates that there was a river attack,
* thus the next player should not get his turn, skip to the next available player.
* 
* The last item in the list points to the head of the list, that way, the turns are handled in a circular manner.
* 
* The nodes are also maintained as a hashmap, where player id is mapped to the player node.
* That way, when a change in one of the items in the list is required, for example change player status
* if no longer active, then the access to that item in the list is faster.
* 
* @author      Keren Solomon
*/
public class TurnsLinkedList {
	private static final int FIRST_PLAYER_INDEX = 0;
	private static final int SEC_PLAYER_INDEX = 1;
	
	private Boolean blocked;
	private Node head;
	private Node currPlayer;
	private Map<String, Node> playeridToNodeMap;
	
	public TurnsLinkedList() {
		head = null;
		currPlayer = null;
		blocked = false;
		playeridToNodeMap = new HashMap<>();
	}
	
	/**
	 * Node class is a private class in the TurnsLinkedList class.
	 * The class defined a Node object, which represents a player in the turns linked list.
	 * 
	 * Each node has a player is and a player state (active / not active).
	 * if the node state is not active - the list will skip his turn to the next player.
	 * 
	 * Each Node holds a 'next' member, that points to the next node in the list,
	 * and a 'prev' member, that points to the previous node in the list.
	 */
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
	
	public String getCurrentPlayerId() {
		return currPlayer.playerId;
	}
	
	/**
	 * This method is called by the Game class, when the game is about to start.
	 * The data of each player is saved as Nodes in the list,
	 * and also inserted to the players-to-nodes map.
	 * 
	 * This method sets the current player as the head player.
	 * 
	 * @param players  the list of players in the game.
	 * 
	 */
	public void addPlayers(List<Player> players) {
		/* Note: the game can have minimum 2 players */
		head = new Node(players.get(FIRST_PLAYER_INDEX).getId());
		playeridToNodeMap.put(head.playerId, head);
		
		Node node = head;
		for (int i = SEC_PLAYER_INDEX; i <players.size(); i++) {
			node.next = new Node(players.get(i).getId());
			node.next.prev = node;
			node = node.next;
			playeridToNodeMap.put(node.playerId, node);
		}
		node.next = head;
		head.prev = node;
		
		currPlayer = head;
	}
	
	/**
	 * This method is called in one of two cases:
	 * when there is a river attack in game, set to true, as the next player should be blocked.
	 * when the attack ended, player turn was skipped, set to false, as no longer need to block players.
	 * 
	 * @param state  the boolean that indicates whether the next player is blocked.
	 * 
	 */
	public void setBlocked(Boolean state) {
		blocked = state;
	}
	
	/**
	 * This method is called when a player loses the game, his state needs to be set to not active.
	 * 
	 * @param playerId  the id of the player that his status needs to be changed.
	 * 
	 */
	public void setPlayerNotActive(String playerId) {
		Node player = playeridToNodeMap.get(playerId);
		player.isActive = false;
	}
	
	/**
	 * This method is called by the Game class, when a player finishes it's turn.
	 * 
	 * The method calls a helper method to get the next player available in the list,
	 * checks if that player is not blocked, otherwise call the helper method again.
	 */
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
	
	/**
	 * This method is called by this class, when needs to get the next playing player turn.
	 * 
	 * This method is a helper method for the nextPlayerTurn, it's purpose is to find the
	 * next available player in the list.
	 * 
	 * It starts from the given node, checks the adjacent player in the list, next to it.
	 * if the player state is active, it is returned.
	 * Otherwise, keep this process until an active player is found.
	 * 
	 * @param fromPlayer  the node that represents the player to start check from.
	 * 
	 * @return  Node	  the node that represents the next active player found.
	 */
	public Node getNextActivePlayer(Node fromPlayer) {
		Node nextPlayer = fromPlayer.next;
		while (!nextPlayer.isActive) {
			nextPlayer = nextPlayer.next;
		}
		return nextPlayer;
	}
}
