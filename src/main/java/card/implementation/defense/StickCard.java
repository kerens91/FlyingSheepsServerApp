package card.implementation.defense;

import card.interfaces.IDefenseCard;
import card.types.AbstractPlayableCard;
import database.entity.CardEntity;
import game.players.Player;

/**
* This class represents the Shepherd Stick Card.
* Shepherd Stick card is a defense card,
* meaning that the card allows the player to protect himself from an attack.
* 
* This class inherits from the AbstractPlayableCard class, it means that the card can be
* played by the player (not only by the deck), a player can pick and play the card during the game.
* This class implements the IDefenseCard interface, that gives the card it's
* defense functionalities.
* 
* A Stick defense card will protect the player in case of a nature disaster attack.
* If the player is a victim to a nature disaster attack (pit / cliff / avalanche), it means that
* your herd encountered a disaster and you might lose the game, but if your shepherd use the 
* shepherd stick - the attack is failed and you get to stay in the game.
* 
* @author      Keren Solomon
*/
public class StickCard extends AbstractPlayableCard implements IDefenseCard {

	public StickCard(CardEntity cEntity, int gameId) {
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
	 * the Shepherd Stick card in it's turn.
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
	 * This method indicates that the shepherd stick card is not useful in case of a steal attack.
	 * 
	 * @return  true|false	true if the card can defend from a steal attack, otherwise false.
	 */
	@Override
	public Boolean isStealDefenseCard() {
		return false;
	}

	/**
	 * This method indicates that the shepherd stick card can be used as a defense only
	 * in case of a nature disaster attack, thus return true for pit defense.
	 * 
	 * @return  true|false	true if the card can defend from a pit attack, otherwise false.
	 */
	@Override
	public Boolean isPitDefenseCard() {
		return true;
	}

	/**
	 * This method indicates that the shepherd stick card can be used as a defense only
	 * in case of a nature disaster attack, thus return true for cliff defense.
	 * 
	 * @return  true|false	true if the card can defend from a cliff attack, otherwise false.
	 */
	@Override
	public Boolean isCliffDefenseCard() {
		return true;
	}

	/**
	 * This method indicates that the shepherd stick card can be used as a defense only
	 * in case of a nature disaster attack, thus return true for avalanche defense.
	 * 
	 * @return  true|false	true if the card can defend from an avalanche attack, otherwise false.
	 */
	@Override
	public Boolean isAvalancheDefenseCard() {
		return true;
	}

}
