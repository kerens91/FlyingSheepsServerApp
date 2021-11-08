package game.gameplayers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import card.types.AbstractPlayableCard;
import card.types.AbstractValueableCard;
import eventnotifications.IPlayerNotifications;
import game.GameManager;


public class Player {
	private static final Logger logger = LogManager.getLogger(Player.class);
	private volatile String playerId;
	private volatile String name;
	private volatile Boolean isActive;
	private volatile String img;
	private volatile int score;
	private List<AbstractCard> hand;
	private IPlayerNotifications playerNotifications;
	
	public Player(String id, String name, String img) {
		playerId = id;
		isActive = true;
		score = 0;
		hand = Collections.synchronizedList(new ArrayList<AbstractCard>());
		this.name = name;
		this.img = img;
	}
	
	public void registerCallback () {
    	this.playerNotifications = GameManager.getInstance();
    }
	
	public void addCardToPlayerHand(AbstractCard card) {
		hand.add(card);
		notifyPlayerEventAddCard(card);
	}
	
	public void removeCardFromPlayerHand(AbstractCard card) {
		hand.remove(card);
		notifyPlayerEventRemoveCard(card);
	}
	
	public void addPointsToPlayerScore(int points) {
		score += points;
		notifyPlayerEventUpdatePoints(score);
	}
	
	public void dropPointsFromPlayerScore(int points) {
		score -= points;
		notifyPlayerEventUpdatePoints(score);
	}
	
	public List<AbstractCard> getHand() {
		return hand;
	}
	
	public int getPlayerScore() {
		return score;
	}
	
	public String getId() {
		return playerId;
	}

	public String getName() {
		return name;
	}
	
	public int getNumOfCards() {
		return hand.size();
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isActive() {
		return isActive;
	}
	
	public void setActive(Boolean active) {
		this.isActive = active;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
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
	
	public void removeAllExceptFlyingSheepCards(int cardId) {
		removeAllExceptFlyingSheepCardsHelper(cardId, 0);
	}

	public AbstractCard getCardInIndex(int index) {
		AbstractCard card = hand.get(index);
		return card;
	}
	
    private void notifyPlayerEventAddCard(AbstractCard card) {
    	if (playerNotifications != null) {
    		playerNotifications.playerHandCardAdded(playerId, card.getId());
    	}
    	else {
    		logger.error("callback is null");
    	}
    }
    
    private void notifyPlayerEventRemoveCard(AbstractCard card) {
    	if (playerNotifications != null) {
    		playerNotifications.playerHandCardRemoved(playerId, card.getId());
    	}
    	else {
    		logger.error("callback is null");
    	}
    }
    
    private void notifyPlayerEventUpdatePoints(int points) {
    	if (playerNotifications != null) {
    		playerNotifications.playerHandUpdatePoints(playerId, points);
    	}
    	else {
    		logger.error("callback is null");
    	}
    }
}
