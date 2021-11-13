package card.implementation.defense;

import card.interfaces.IDefenseCard;
import card.types.AbstractValueableCard;
import game.gameplayers.Player;

/**
* This class represents the Flying Sheep Card.
* Flying Sheep card is a defense card,
* meaning that the card allows the player to protect himself from an attack.
* 
* This class inherits from the AbstractValueableCard class, it means that the card has a value,
* thus increasing the number of points of the player that holds that card.
* Also, a valuable card is a playable card, thus, the card can be played by the player (not only by the deck),
* a player can pick and play the card during the game.
* This class implements the IDefenseCard interface, that gives the card it's
* defense functionalities.
* 
* A flying sheep defense card will protect the player in case of a nature disaster attack.
* If the player is a victim to a nature disaster attack of types pit and cliff, it means that
* your herd encountered a disaster and you might lose the game, but if you use your flying sheeps,
* you can avoid the pit / cliff disaster and the attack is failed, you get to stay in the game but you
* loose all other cards except the flying sheeps.
* 
* @author      Keren Solomon
*/
public class FlyingSheepCard extends AbstractValueableCard implements IDefenseCard {
	public FlyingSheepCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back, int value, String points) {
		super(typeId, gameId, name, txtColor, img, frame, back, value, points);
	}

	/**
	 * This method is called when a player in game picked to play
	 * the flying sheep card in it's turn.
	 * 
	 * call the picked defense card method to handle the defense.
	 * 
	 * @param player  the Player that picked the card.
	 */
	@Override
	public void playerPickedCard(Player player) {
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
		cardNotifications.defenseCardPickedFlyingSheep(this, player);
	}
	
	/**
	 * This method indicates that the flying sheep card is not useful in case of a steal attack.
	 * 
	 * @return  true|false	true if the card can defend from a steal attack, otherwise false.
	 */
	@Override
	public Boolean isStealDefenseCard() {
		return false;
	}

	/**
	 * This method indicates that the flying sheep card can be used as a defense only
	 * in case of a pit or cliff attacks, thus return true for pit defense.
	 * 
	 * @return  true|false	true if the card can defend from a pit attack, otherwise false.
	 */
	@Override
	public Boolean isPitDefenseCard() {
		return true;
	}

	/**
	 * This method indicates that the flying sheep card can be used as a defense only
	 * in case of a pit or cliff attacks, thus return true for cliff defense.
	 * 
	 * @return  true|false	true if the card can defend from a cliff attack, otherwise false.
	 */
	@Override
	public Boolean isCliffDefenseCard() {
		return true;
	}

	/**
	 * This method indicates that the flying sheep card is not useful in case of an avalanche attack.
	 * 
	 * @return  true|false	true if the card can defend from an avalanche attack, otherwise false.
	 */
	@Override
	public Boolean isAvalancheDefenseCard() {
		return false;
	}

}
