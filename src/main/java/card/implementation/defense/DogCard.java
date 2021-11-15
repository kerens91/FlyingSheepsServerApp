package card.implementation.defense;

import card.interfaces.IDefenseCard;
import card.types.AbstractPlayableCard;
import database.entity.CardEntity;
import game.players.Player;

/**
* This class represents the Shepherd Dog Card.
* Dog card is a defense card,
* meaning that the card allows the player to protect himself from an attack.
* 
* This class inherits from the AbstractPlayableCard class, it means that the card can be
* played by the player (not only by the deck), a player can pick and play the card during the game.
* This class implements the IDefenseCard interface, that gives the card it's
* defense functionalities.
* 
* A Dog defense card will protect the player in case of a steal attack.
* If the player is a victim to a steal attack, it means that one of the other 
* players is trying to steal a card from you, but if you use the dog card - the
* attack is failed and you get to keep all your cards (except of the used dog card).
* 
* @author      Keren Solomon
*/
public class DogCard extends AbstractPlayableCard implements IDefenseCard {
	
	public DogCard(CardEntity cEntity, int gameId) {
		super(cEntity.getId(),
				gameId,
				cEntity.getName(),
				cEntity.getDecore().getTxtCol(),
				cEntity.getDecore().getImg(),
				cEntity.getDecore().getFrameImg(),
				cEntity.getDecore().getBackImg());
	}
	
	/**
	 * This method is called when a player in game picked to play
	 * the dog card in it's turn.
	 * 
	 * The card is marked as used, meaning remove from player hand.
	 * call the picked defense card method to handle the defense.
	 * 
	 * @param player  the Player that picked the card.
	 */
	@Override
	public void playerPickedCard(Player player) {
		setCardUsed(player);
		pickedDefenseCard(player);
	}
	
	/**
	 * This method triggers event handled by the GameManager class,
	 * to try and stop the ongoing attack, and to inform the players with the
	 * defense attempt.
	 * 
	 * @param player  the Player that picked the card.
	 */
	@Override
	public void pickedDefenseCard(Player player) {
		cardNotifications.defenseCardPicked(this, player);
	}

	/**
	 * This method indicates that the dog card can be used as a defense only
	 * in case of a steal attack, thus return true for steal defense card method.
	 * 
	 * @return  true|false	true if the card can defend from a steal attack, otherwise false.
	 */
	@Override
	public Boolean isStealDefenseCard() {
		return true;
	}

	/**
	 * This method indicates that the dog card is not useful in case of a pit attack.
	 * 
	 * @return  true|false	true if the card can defend from a pit attack, otherwise false.
	 */
	@Override
	public Boolean isPitDefenseCard() {
		return false;
	}

	/**
	 * This method indicates that the dog card is not useful in case of a cliff attack.
	 * 
	 * @return  true|false	true if the card can defend from a cliff attack, otherwise false.
	 */
	@Override
	public Boolean isCliffDefenseCard() {
		return false;
	}

	/**
	 * This method indicates that the dog card is not useful in case of an avalanche attack.
	 * 
	 * @return  true|false	true if the card can defend from an avalanche attack, otherwise false.
	 */
	@Override
	public Boolean isAvalancheDefenseCard() {
		return false;
	}
}
