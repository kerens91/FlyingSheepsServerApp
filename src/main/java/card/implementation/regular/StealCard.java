package card.implementation.regular;

import java.util.List;

import attackstate.interfaces.IAttackDefensable;
import card.AbstractCard;
import card.interfaces.IDefenseCard;
import card.types.AbstractAttackCard;
import database.entity.StringEntity;
import game.players.Player;

/**
* This class represents the Steal Card.
* Steal card is an attack card.
* 
* The steal card is not an actual card in the game, the deck does not contain
* the steal card, players cannot play this card.
* This card is created by the cards factory, and is only used when a regular couple cards
* are picked by the player.
* In this case, this card is set as the attack card for the attack states.
* 
* This class inherits from the AbstractAttackCard class,
* meaning that the card implements the attack methods.
* This class implements the IAttackDefensable interface,
* it means that the attack can fail if the attacked player defended himself.
* 
* A Steal attack is attack carried out by a player,
* against another player in the game.
* 
* In this type of attack, a player can try to defend himself, using one of the defense card.
* Only the Shepherd Dog defense card can protect a player from a Steal attack.
* If the attack succeeded (the player did not defended himself), the attacked player loses a random card,
* and the attacker player gets the card.
* 
* @author      Keren Solomon
*/
public class StealCard extends AbstractAttackCard implements IAttackDefensable {
	public StealCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back, int value, String points, List<StringEntity> strings) {
		super(typeId, gameId, name, txtColor, img, frame, back, value, points, strings);
	}
	
	/**
	 * This method indicates that the pre-attack state is needed
	 * for the steal attack, in order to get the victim for the attack.
	 * 
	 * @return  true|false	true if an action prior to the attack is needed, otherwise false.
	 */
	@Override
	public Boolean preAttackNeeded() {
		return true;
	}

	/**
	 * 
	 * This method is called when a player started the Steal attack against another player.
	 * An event is triggered to inform the GameManager with the attack.
	 * A message with attack information will be sent to all players in the game.
	 */
	@Override
	public void doAttack() {
		cardNotifications.notifyAttackOnPlayer();
	}

	/**
	 * 
	 * This method is called when the attacked player did not managed to defend himself.
	 * An event is triggered to inform the GameManager with the attack success state.
	 * A message with attack information will be sent to all players in the game.
	 * The Game class will handle the attack - remove a random card from victim player hand,
	 * add it to the attacker player hand.
	 */
	@Override
	public void attackSucceeded() {
		cardNotifications.stealAttackSucceeded();
	}
	
	/**
	 * 
	 * This method is called when the attacked player managed to defend himself using a defense card.
	 * An event is triggered to inform the GameManager with the attack failure state.
	 * A message with attack information will be sent to all players in the game.
	 */
	@Override
	public void attackFailed() {
		cardNotifications.stealAttackFailed();
	}

	/**
	 * 
	 * This method is called when this card is used for the attack and the card is presented
	 * in the used cards pile.
	 */
	@Override
	public void playerPickedCard(Player player) {
		setCardUsed(player);
	}

	/**
	 * This method is called when the victim tries to defend himself from the steal attack
	 * The attack will fail only if the victim managed to use the Shepherd Dog defense card.
	 * 
	 * @param defenseCard  the card that was used by the player as defense against the attack.
	 * 
	 * @return  true|false	true if the defense succeeded, otherwise false.
	 */
	@Override
	public Boolean defenseSucceeded(AbstractCard defenseCard) {
		return (((IDefenseCard) defenseCard).isStealDefenseCard());
	}
}
