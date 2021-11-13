package card.types;

import card.interfaces.ICoupledCard;
import game.gameplayers.Player;

/**
* Abstract Ownerable Card class defines the cards that can have owners.
* The card owner is the player that holds it.
*  
* The ownerable cards in game are:
* <ul>
* <li>Nuclear Bomb card
* <li>Super Flying Sheep card
* <li>Flying Husband Sheep card
* <li>Flying Wife Sheep card
* </ul>
* 
* This class inherits from the AbstractPlayableCard class,
* thus, the card can be played by the player (not only by the deck).
* This class implements the ICoupledCard interface, that obligates the inheritance classes to implement
* the Couple functionalities.
* that gives the card a different meaning when used as a coupled.
* 
* @author      Keren Solomon
*/
public abstract class AbstractOwnerableCard extends AbstractPlayableCard implements ICoupledCard {
	private String owner = null;
	
	public AbstractOwnerableCard(int typeId, int gameId, String name, int textColor, String img, String frame, String back) {
		super(typeId, gameId, name, textColor, img, frame, back);
	}
	
	/**
	 * This method is called when a player draws this card from the deck.
	 * It sets the player as the owner of the card.
	 * An event is triggered to inform the GameManager with the new owners.
	 * 
	 * This method indicates about the current player turn if ended.
	 * 
	 * @param player  the player that got this card from deck.
	 * 
	 * @return  true|false	true if the turn ended, otherwise false.
	 */
	public Boolean playCardFromDeck(Player player) {
		setOwners(player.getId());
		cardNotifications.onGotFromDeckSpecial(getId());
		return super.playCardFromDeck(player);
	}

	/**
	 * This method is called when the card is used by a player.
	 * It sets the card as used (remove from player hand).
	 * 
	 * @param player  the player that picked the card.
	 * 
	 */
	@Override
	public void playerPickedCard(Player player) {
		setCardUsed(player);
	}
	
	/**
	 * This method is called when a player picked 2 cards.
	 * Both cards are marked as used, meaning removed from player hand.
	 * Check if this is couple are matching couple - call do couple method.
	 * 
	 * @param player  the Player that picked the cards.
	 * @param card    the second card in the couple (first is this class card).
	 * 
	 */
	@Override
	public void playerPickedCouple(Player player, AbstractPlayableCard card) {
		super.playerPickedCouple(player, card);
		if (isCouple(card)) {
			doCouple(player);
		}	
	}
	
	public void setOwners(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}
	
	public abstract cardFeatureType getSpecialFeatureType();
	
	public enum cardFeatureType {
		FEATURE_TYPE_COOP,
		FEATURE_TYPE_WIN
	}
}
