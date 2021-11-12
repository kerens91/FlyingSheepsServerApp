package card.implementation.attack;

import java.util.List;

import card.types.AbstractAttackCard;
import database.entity.StringEntity;
import game.gameplayers.Player;

/**
* This class represents the Rock Card.
* Rock card is an attack card,
* meaning that the card allows the player to launch an attack.
* 
* This class inherits from the AbstractAttackCard class,
* thus implementing all it's methods.
* 
* A rock attack is attack carried out by a player,
* against another chosen player,
* to make the attacked player to lose one of his cards.
* 
* A rock attack is not a defensable attack, thus when it starts
* the attack result state is changing to attack succeeded,
* and all players are informed - that a player attacked another player
* with a rock.
* 
* @author      Keren Solomon
*/
public class RockCard extends AbstractAttackCard {
	public RockCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back, int val, String points, List<StringEntity> strings) {
		super(typeId, gameId, name, txtColor, img, frame, back, val, points, strings);
	}
	
	/**
	 * This method is called when a player in game picked to play
	 * the rock card in it's turn.
	 * 
	 * The card is marked as used, meaning remove from player hand.
	 * The picked attack card method is called to trigger an event 
	 * to start the attack.
	 * 
	 * @param player  the Player that picked the card.
	 */
	@Override
	public void playerPickedCard(Player player) {
		setCardUsed(player);
		pickedAttackCard(this, player);
	}
	
	/**
	 * This method indicates that the pre-attack state is needed
	 * for the rock attack, in order to get the victim for the attack
	 */
	@Override
	public Boolean preAttackNeeded() {
		return true;
	}

	/**
	 * This method calls an event to start the rock attack,
	 * it means that the victim player loses one of his cards.
	 */
	@Override
	public void doAttack() {
		cardNotifications.doRockAttack();
	}
	
	/**
	 * This method calls an event to finish attack.
	 * It handles the case in which a rock attack succeeded,
	 * it means that all players need to get information about the attack.
	 */
	@Override
	public void attackSucceeded() {
		cardNotifications.rockAttackSucceeded();
	}
}
