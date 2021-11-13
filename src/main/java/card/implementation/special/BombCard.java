package card.implementation.special;

import static card.types.AbstractOwnerableCard.cardFeatureType.FEATURE_TYPE_WIN;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import game.gameplayers.Player;

/**
* This class represents the Nuclear Bomb Card.
* Bomb card is a special card,
* meaning it has a special functionality that other cards don't have.
*  
* The bomb card, if used as coupled with the Super-Flying Seep card, will end the game,
* and the player who used this couple is the winner.
* A player can use the couple Nuclear Bomb card and Super-Flying Seep card to win the game
* only if he is the owner of both cards.
* 
* There is only a single Nuclear Bomb card in the game.
* 
* This class inherits from the AbstractOwnerableCard class,
* meaning that the card can have owners - the player that holds it.
* 
* @author      Keren Solomon
*/
public class BombCard extends AbstractOwnerableCard {
	public BombCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back) {
		super(typeId, gameId, name, txtColor, img, frame, back);
	}

	/**
	 * This method indicates that the cards are matching couple -
	 * check that the other card is a Super-Flying Seep card.
	 * 
	 * @param card    the second card in the couple (first is this class card).
	 * 
	 * @return  true|false	true if the cards are matching couple, otherwise false.
	 */
	@Override
	public Boolean isCouple(AbstractCard card) {
		return (card instanceof SuperFlyingCard);
	}
	
	/**
	 * This method is called when the player picked a couple - 
	 * this Nuclear Bomb card and a Super-Flying Seep card.
	 * In this case, the player is the game winner.
	 * 
	 * Call an event to notify the GameManager class that the game is over.
	 * 
	 * @param player  the Player that picked the cards.
	 * 
	 */
	@Override
	public void doCouple(Player player) {
		cardNotifications.specialCoupleWinGame(player);
	}
	
	/**
	 * This method returns type WIN as the feature type of this card.
	 *  
	 * @return  cardFeatureType	the feature type of this card.
	 */
	@Override
	public cardFeatureType getSpecialFeatureType() {
		return FEATURE_TYPE_WIN;
	}
}
