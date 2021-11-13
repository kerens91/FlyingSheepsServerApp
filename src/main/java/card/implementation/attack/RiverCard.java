package card.implementation.attack;

import java.util.List;

import card.types.AbstractAttackCard;
import database.entity.StringEntity;
import game.players.Player;

/**
* This class represents the River Card.
* River card is an attack card,
* meaning that the card allows the player to launch an attack.
* 
* This class inherits from the AbstractAttackCard class,
* thus implementing all it's methods.
* It means that when the card is used by the player, an attack starts.
* 
* A river attack is attack carried out by a player,
* against the player whose next turn.
* 
* A river attack is not a defensable attack, thus when it starts
* the attack result state is changing to attack succeeded,
* and all players are informed - that the next player is blocked.
* 
* @author      Keren Solomon
*/
public class RiverCard extends AbstractAttackCard {
	public RiverCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back, int val, String points, List<StringEntity> strings) {
		super(typeId, gameId, name, txtColor, img, frame, back, val, points, strings);
	}
	
	/**
	 * This method is called when a player in game picked to play
	 * the river card in it's turn.
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
	 * This method indicates that the pre-attack state is not needed
	 * for the river attack
	 * 
	 * @return  true|false	true if an action prior to the attack is needed, otherwise false.
	 */
	@Override
	public Boolean preAttackNeeded() {
		return false;
	}
	
	/**
	 * This method calls an event to start the river attack,
	 * it means that the game has to skip the next player turn in this round.
	 */
	@Override
	public void doAttack() {
		cardNotifications.doRiverAttack();
	}

	/**
	 * This method calls an event to finish attack.
	 * It handles the case in which a river attack succeeded,
	 * it means that all players need to get information about the attack.
	 */
	@Override
	public void attackSucceeded() {
		cardNotifications.riverAttackSucceeded();
	}
}
