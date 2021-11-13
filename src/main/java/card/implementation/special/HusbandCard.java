package card.implementation.special;

import static card.types.AbstractOwnerableCard.cardFeatureType.FEATURE_TYPE_COOP;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import game.gameplayers.Player;

/**
* This class represents the Flying Husband Sheep Card.
* husband card is a special card,
* meaning it has a special functionality that other cards don't have.
*  
* The husband card, if used as coupled with the Flying Wife Sheep card, will enable
* the disqualification of a player from the game.
* 
* A player can cooperate with another player, only if each one of the players is holding one
* of the matching couple - Flying Wife Sheep card and the Flying Husband Sheep card.
* 
* There is only a single Flying Husband Sheep card in the game.
* 
* This class inherits from the AbstractOwnerableCard class,
* meaning that the card can have owners - the player that holds it.
* 
* THIS IS NOT YET IMPLEMENTED
* 
* @author      Keren Solomon
*/
public class HusbandCard extends AbstractOwnerableCard {
	public HusbandCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back) {
		super(typeId, gameId, name, txtColor, img, frame, back);
	}

	@Override
	public Boolean isCouple(AbstractCard card) {
		return (card instanceof WifeCard);
	}
	
	@Override
	public void doCouple(Player player) {
		cardNotifications.specialCoupleShowCoopBtn();
	}

	@Override
	public cardFeatureType getSpecialFeatureType() {
		return FEATURE_TYPE_COOP;
	}
	
}
