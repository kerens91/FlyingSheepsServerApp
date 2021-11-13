package card.types;

import java.util.List;
import java.util.Map;

import attackmsg.AttackMsgInfo;
import attackstate.interfaces.IAttackStatable;
import card.AbstractCard;
import database.entity.StringEntity;
import game.players.Player;

/**
* Abstract Attack Card class defines a type of card - attack card.
* Attack card is used to start an attack against another player or all players.
* 
* The possible attack cards:
* <ul>
* <li>River card
* <li>Rock card
* <li>Tree card
* <li>Steal card
* </ul>
* 
* This class inherits from the AbstractValueableCard class, it means that the card has a value,
* thus increasing the number of points of the player that holds that card.
* Also, a valuable card is a playable card, thus, the card can be played by the player (not only by the deck),
* a player can pick and play the card during the game.
* Once a player picks attack card - the attack starts and handled by the AttackHandler class, operated in states.
* 
* This class implements the IAttackStatable interface, that obligates the inheritance classes to implement
* the attack functionalities divided by states.
* 
* Each attack card will have a list of strings, related to the attack, to be presented to the players
* during the game. Each string message have a state in which it needs to appear, and a destination - who is
* receiving the attack message (the attacker / the victim / all players).
* 
* @author      Keren Solomon
*/
public abstract class AbstractAttackCard extends AbstractValueableCard implements IAttackStatable {
	private Map<Integer,Map<Integer,AttackMsgInfo>> cardStrings;
	
	public AbstractAttackCard(int typeId, int gameId, String name, int txtCol, String img, String frame, String back, int value, String points, List<StringEntity> strings) {
		super(typeId, gameId, name, txtCol, img, frame, back, value, points);
		cardStrings = initCardsStrings(strings);
	}
	
	/**
	 * This method is called when a player picked an attack card.
	 * An event is triggered to inform the GameManager class with the attack information.
	 * 
	 * @param card    the picked attack card.
	 * @param player  the Player that picked the card.
	 * 
	 */
	public void pickedAttackCard(AbstractCard card, Player player) {
		cardNotifications.attackCardPicked(card, player);
	}

	/**
	 * This method is called during an attack, when a message needs to be printed to
	 * players screens, with information about the attack.
	 * The string message is accessed based on the attack state and the message receiver.
	 * 
	 * @param state        the int represents the attack current state.
	 * @param destination  the int represents the message receiver.
	 * 
	 */
	@Override
	public AttackMsgInfo getTitle(int state, int destination) {
		return cardStrings.get(state).get(destination);
	}
	
}
