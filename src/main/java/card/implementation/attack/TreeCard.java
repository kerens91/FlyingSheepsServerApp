package card.implementation.attack;

import java.util.List;

import attackstate.interfaces.IAttackFailable;
import card.types.AbstractAttackCard;
import database.entity.StringEntity;
import game.gameplayers.Player;

/**
* This class represents the Tree Card.
* Tree card is an attack card,
* meaning that the card allows the player to launch an attack.
* 
* This class inherits from the AbstractAttackCard class,
* thus implementing all it's methods.
* It means that when the card is used by the player, an attack starts.
* This class implements IAttackFailable interface, that is to say, the
* attack can fail.
* 
* A tree attack is attack carried out by a player,
* against all other players in the game,
* to make the trees tell secrets about the special cards in the game,
* meaning, if one or more of the players in the game are owners
* of one of the special cards - nuclear bomb and super flying sheep.
* 
* A tree attack is not a defensable attack,
* but in case that no one in the game is owner of a special card,
* the trees will return an empty list of owners,
* in this case the attack result state is changing to attack failed,
* otherwise it changes to attack succeeded,
* and all players are informed - that a player got secrets from the trees.
* 
* @author      Keren Solomon
*/
public class TreeCard extends AbstractAttackCard implements IAttackFailable {
	public TreeCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back, int val, String points, List<StringEntity> strings) {
		super(typeId, gameId, name, txtColor, img, frame, back, val, points, strings);
	}

	/**
	 * This method is called when a player in game picked to play
	 * the tree card in it's turn.
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
	 * for the tree attack.
	 */
	@Override
	public Boolean preAttackNeeded() {
		return false;
	}

	/**
	 * This method calls an event to start the tree attack,
	 * it means that the game should generate a list of owners.
	 */
	@Override
	public void doAttack() {
		cardNotifications.doTreeAttack();
	}

	/**
	 * This method calls an event to finish attack.
	 * It handles the case in which a tree attack succeeded,
	 * it means that all players need to get information about the attack,
	 * and the attacker should get the list of owners.
	 */
	@Override
	public void attackSucceeded() {
		cardNotifications.treeAttackSucceeded();
	}

	/**
	 * This method calls an event to finish attack.
	 * It handles the case in which a tree attack failed,
	 * it means that all players need to get information about the attack,
	 * and the attacker should get a message about the empty list.
	 */
	@Override
	public void attackFailed() {
		cardNotifications.treeAttackFailed();
	}
	
}
