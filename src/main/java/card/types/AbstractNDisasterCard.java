package card.types;

import static globals.Constants.CARD_DONT_END_TURN;

import java.util.List;
import java.util.Map;

import attackmsg.AttackMsgInfo;
import attackstate.interfaces.IAttackDefensable;
import attackstate.interfaces.IAttackStatable;
import card.AbstractCard;
import clientservershared.CardModel;
import database.entity.StringEntity;
import game.players.Player;

/**
* Abstract Nature Disaster Card class defines a type of card - nature disaster card.
* nature disaster card is used when a player draws this card from the deck,
* an attack immediately begins (the player is the victim).
* 
* The possible nature disaster cards:
* <ul>
* <li>avalanche card
* <li>pit card
* <li>cliff card
* </ul>
* 
* This class inherits from the basic AbstractCard class.
* This class implements the IAttackStatable interface, that obligates the inheritance classes to implement
* the attack functionalities divided by states.
* This class also implements the IAttackDefensable interface, it means that the victim player can defended himself,
* and the attack can fail or succeeded.
* If the attack succeeded, the attacked player loses the game.
* 
* The attacks are handled by the AttackHandler class.
* This type of attack has no attacker.
* 
* Each Nature Disaster card will have a list of strings, related to the attack, to be presented to the players
* during the game. Each string message have a state in which it needs to appear, and a destination - who is
* receiving the attack message (the victim / all players).
* 
* @author      Keren Solomon
*/
public abstract class AbstractNDisasterCard extends AbstractCard implements IAttackStatable, IAttackDefensable {
	private Map<Integer,Map<Integer,AttackMsgInfo>> cardStrings;
	
	public AbstractNDisasterCard(int typeId, int gameId, String name, String img, List<StringEntity> strings) {
		super(typeId, gameId, name, img);
		cardStrings = initCardsStrings(strings);
	}
	
	/**
	 * This method is called when a player draws this card from the deck.
	 * 
	 * This method indicates that the current player turn is not ended,
	 * compared to the other cases in which a card is drawn from the deck,
	 * as an attack begins and has to wait for it to end in order to finish the player turn.
	 * 
	 * @param player  the player that got this card from deck.
	 * 
	 * @return  true|false	true if the turn ended, otherwise false.
	 */
	public Boolean playCardFromDeck(Player player) {
		natureDisasterAttack(player);
		return CARD_DONT_END_TURN;	
	}
	
	/**
	 * This method is called when a player draws this card from the deck.
	 * An event is triggered to inform the GameManager class with the attack information.
	 * 
	 * @param player  the Player that got this card from deck.
	 * 
	 */
	public void natureDisasterAttack(Player player) {
		cardNotifications.startNatureDisasterAttack(this, player);
	}

	/**
	 * This method indicates that the pre-attack state is not needed for the attack.
	 * 
	 * @return  true|false	true if an action prior to the attack is needed, otherwise false.
	 */
	@Override
	public Boolean preAttackNeeded() {
		return false;
	}
	
	/**
	 * This method triggers an event to inform the GameManager with the attack.
	 */
	@Override
	public void doAttack() {
		cardNotifications.notifyAttackOnPlayer();
	}
	
	/**
	 * This method triggers an event to inform the GameManager with the attack result as success.
	 */
	@Override
	public void attackSucceeded() {
		cardNotifications.natureDisasterAttackSucceeded();
	}

	/**
	 * This method triggers an event to inform the GameManager with the attack result as failure.
	 */
	@Override
	public void attackFailed() {
		cardNotifications.natureDisasterAttackFailed();
	}
	
	/**
	 * This method is called when a message regarding the attack is meant to be sent to one
	 * or more players.
	 * 
	 * @param state  		the int represents the attack current state.
	 * @param destination  	the int represents the message receiver.
	 * 
	 * @return  AttackMsgInfo	attack message information
	 * 
	 */
	@Override
	public AttackMsgInfo getTitle(int state, int destination) {
		return cardStrings.get(state).get(destination);
	}
	
	/**
	 * This method indicated that the card can not be used by players, only by deck.
	 * 
	 * @return  true|false	true if the card can be played by player, otherwise false.
	 * 
	 */
	@Override
		public Boolean isPlayable() {
			return false;
		}
	
	/**
	 * This method is called when the server need to send the card information to the client,
	 * thus using the shared structure CardModel containing the card information.
	 * A new CardModel is created with the card information.
	 * 
	 * @return  CardModel	card information as a CardModel object
	 * 
	 */
	@Override
		public CardModel getCardInfo() {
			return new CardModel(getId(), getName(), getImg(), 0, null, null, null);
		}
}
