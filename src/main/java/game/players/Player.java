package game.players;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import card.types.AbstractPlayableCard;
import card.types.AbstractValueableCard;
import eventnotifications.IPlayerNotifications;
import game.GameManager;

/**
* This class represents a player in the game.
* The player are initiated by the Game class.
* 
* A player will have:
* <ul>
* <li>playerId - represents the id of the player (and the id of the client connection for socket use).
* <li>name - represents the player name.
* <li>img - represents the player image.
* <li>score - this int value holds the score of the player during the game.
* <li>isActive - this boolean represents the status of the player in the game.
* <li>hand - a list of cards that represents the player hand.
* </ul>
* 
* Each player is created with an id, name and image values, and it is not changeable.
* This values are set by the player itself when connecting to the game.
* 
* @author      Keren Solomon
*/
public class Player {
	private static final Logger logger = LogManager.getLogger(Player.class);
	
	private final String playerId;
	private final String name;
	private final String img;
	private Boolean isActive;
	private int score;
	
	private List<AbstractCard> hand;
	private IPlayerNotifications playerNotifications;
	
	/**
	 * Creates a Player to represent a player in the game.
	 * The Player class is created with a player id, a name and an image.
	 * 
	 * The player status is set to 'active', the score is initiated to 0,
	 * and the hand list is initiated as an empty list.
	 * 
	 * @param playerId    the string represents the player's id.
	 * @param name    	  the string represents the player's name.
	 * @param img         the string represents the player's image.
	 */
	public Player(String playerId, String name, String img) {
		this.playerId = playerId;
		this.name = name;
		this.img = img;
		
		isActive = true;
		score = 0;
		hand = new ArrayList<AbstractCard>();
	}
	
	/**
	* This method registers the game manager as a callback handler to this player events.
    * 
	*/
	public void registerCallback () {
    	this.playerNotifications = GameManager.getInstance();
    }
	
	/**
	* This method adds a card to the player's hand.
	* then, notify the client with the new card added.
    * 
	* @param   card    	the card to be added to the hand.
	*/
	public void addCardToPlayerHand(AbstractCard card) {
		hand.add(card);
		notifyPlayerEventAddCard(card);
	}
	
	/**
	* This method removes a card from the player's hand.
	* then, notify the client with the new removed card.
    * 
	* @param   card    	the card to be removed from hand.
	*/
	public void removeCardFromPlayerHand(AbstractCard card) {
		hand.remove(card);
		notifyPlayerEventRemoveCard(card);
	}
	
	/**
	* This method add points to player score.
	* then, notify the client with the new score.
	* 
	* This is called when the player got a new valueable card to his hand.
    * 
	* @param   points    the int represents the new card value.
	*/
	public void addPointsToPlayerScore(int points) {
		score += points;
		notifyPlayerEventUpdatePoints(score);
	}
	
	/**
	* This method subtracts points from player score.
	* then, notify the client with the new score.
	* 
	* This is called when the player loses/used a valueable card.
    * 
	* @param   points    the int represents the removed card value.
	*/
	public void dropPointsFromPlayerScore(int points) {
		score -= points;
		notifyPlayerEventUpdatePoints(score);
	}
	
	/**
	* This method returns the player's hand list of cards.
    * 
	* @return  List<AbstractCard> that represents the player's hand cards.
	*/
	public List<AbstractCard> getHand() {
		return hand;
	}
	
	/**
	* This method returns the player's score.
    * 
	* @return  score the int represents the player's score.
	*/
	public int getPlayerScore() {
		return score;
	}
	
	/**
	* This method returns the player's id.
    * 
	* @return  playerId the string represents the player's id.
	*/
	public String getId() {
		return playerId;
	}

	/**
	* This method returns the player's name.
    * 
	* @return  name the string represents the player's name.
	*/
	public String getName() {
		return name;
	}
	
	/**
	* This method returns the player's number of cards in hand.
    * 
	* @return  the int represents the player's number of cards in hand.
	*/
	public int getNumOfCards() {
		return hand.size();
	}

	/**
	* This method returns the player's status.
    * 
	* @return  true|false	true if the player is active, otherwise false.
	*/
	public Boolean isActive() {
		return isActive;
	}
	
	/**
	* This method updates the player's status.
	* 
	* @param   active    the boolean represents the new player's status.
	*/
	public void setActive(Boolean active) {
		this.isActive = active;
	}

	/**
	* This method returns the player's image.
    * 
	* @return  img	the string represents the player's image.
	*/
	public String getImg() {
		return img;
	}

	/**
	* This method is a helper method for the removeAllExceptFlyingSheepCards method.
	* It is responsible for removing all the cards from player's hand, except of 
	* the cards which are of type 'flying sheep' cards.
	* 
	* This method is is called recursively until the only card remained in the player's hand
	* is the flying sheep defense card.
	* 
	* @param   cardId    	the int represents the index of the defense card.
	* @param   index    	the int represents the running index of the checked card.
	*/
	private void removeAllExceptFlyingSheepCardsHelper(int cardId, int index) {
		if (hand.size() == 1) {
			return;
		}
		
		if (hand.get(index).getId() == cardId) {
			removeAllExceptFlyingSheepCardsHelper(cardId, index+1);
		}
		else {
			AbstractCard c = hand.get(index);
			((AbstractPlayableCard) c).setCardUsed(this);
			removeCardFromPlayerHand(c);
			if (c instanceof AbstractValueableCard) {
				dropPointsFromPlayerScore(((AbstractValueableCard) c).getPoints());
			}
			removeAllExceptFlyingSheepCardsHelper(cardId, index);
		}
	}
	
	/**
	* This method is responsible for removing all the cards from player's hand, except of 
	* the cards which are of type 'flying sheep' cards.
	* 
	* This method is called when the player got a nature disaster card from deck, and
	* used the flying sheep card to defend himself.
	* 
	* This method is calling a helper method - removeAllExceptFlyingSheepCardsHelper.
    * 
	* @param   cardId    	the int represents the index of the defense card.
	*/
	public void removeAllExceptFlyingSheeps(int cardId) {
		removeAllExceptFlyingSheepCardsHelper(cardId, 0);
	}

	/**
	* This method gets a card from the player's hand, based on a given index.
    * 
	* @param   index    	the int represents an index in the hand list of cards.
	* @return  AbstractCard that represents the card in the given index.
	*/
	public AbstractCard getCardInIndex(int index) {
		return hand.get(index);
	}
	
	/**
	* This method is responsible for triggering the event in case a card was added
	* to the player's hand.
    * 
	* @param   card    	the AbstractCard represents the added card.
	*/
    private void notifyPlayerEventAddCard(AbstractCard card) {
    	if (playerNotifications != null) {
    		playerNotifications.playerHandCardAdded(playerId, card.getId());
    	}
    	else {
    		logger.error("callback is null");
    	}
    }
    
    /**
	* This method is responsible for triggering the event in case a card was removed
	* from the player's hand.
    * 
	* @param   card    	the AbstractCard represents the removed card.
	*/
    private void notifyPlayerEventRemoveCard(AbstractCard card) {
    	if (playerNotifications != null) {
    		playerNotifications.playerHandCardRemoved(playerId, card.getId());
    	}
    	else {
    		logger.error("callback is null");
    	}
    }
    
    /**
	* This method is responsible for triggering the event in case that the player's score is updated.
    * 
	* @param   points    	the int represents the updated number of points.
	*/
    private void notifyPlayerEventUpdatePoints(int points) {
    	if (playerNotifications != null) {
    		playerNotifications.playerHandUpdatePoints(playerId, points);
    	}
    	else {
    		logger.error("callback is null");
    	}
    }
}
