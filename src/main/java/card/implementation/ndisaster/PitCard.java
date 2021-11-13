package card.implementation.ndisaster;

import java.util.List;

import card.AbstractCard;
import card.interfaces.IDefenseCard;
import card.types.AbstractNDisasterCard;
import database.entity.StringEntity;

/**
* This class represents the Pit (nature disaster) Card.
* It means that there is a pit down the road, and your herd is in danger.
* Pit card is an attack card,
* meaning that when a player draws this card from the deck, an attack immediately begins (the player is the victim).
* 
* This class inherits from the AbstractNDisasterCard class, it means that the card is a nature disaster type of card,
* thus cannot be played by the players, but can by drawn from the deck, and start the attack on the player.
* 
* In this type of attack, a player can try to defend himself, using one of the defense card.
* Only the Shepherd Stick and the Flying Sheep defense cards can protect a player from a pit attack.
* If the attack succeeded (the player did not defended himself), the attacked player loses the game.
* 
* @author      Keren Solomon
*/
public class PitCard extends AbstractNDisasterCard {
	public PitCard(int typeId, int gameId, String name, String img, List<StringEntity> strings) {
		super(typeId, gameId, name, img, strings);
	}

	/**
	 * This method is called when a player managed to use a defense card
	 * to try defend himself from the pit attack.
	 * 
	 * @param defenseCard  the card that was used by the player as defense against the attack.
	 * 
	 * @return  true|false	true if the defense succeeded, otherwise false.
	 */
	@Override
	public Boolean defenseSucceeded(AbstractCard defenseCard) {
		return (((IDefenseCard) defenseCard).isPitDefenseCard());
	}
}
