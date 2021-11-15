package card.implementation.special;

import static card.types.AbstractOwnerableCard.cardFeatureType.FEATURE_TYPE_COOP;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import card.types.AbstractOwnerableCard.cardFeatureType;
import database.entity.CardEntity;
import game.players.Player;

/**
* This class represents the Flying Wife Sheep Card.
* Wife card is a special card,
* meaning it has a special functionality that other cards don't have.
*  
* The wife card, if used as coupled with the Flying Husband Sheep card, will enable
* the disqualification of a player from the game.
* 
* A player can cooperate with another player, only if each one of the players is holding one
* of the matching couple - Flying Wife Sheep card and the Flying Husband Sheep card.
* 
* There is only a single Flying Wife Sheep card in the game.
* 
* This class inherits from the AbstractOwnerableCard class,
* meaning that the card can have owners - the player that holds it.
* 
* THIS IS NOT YET IMPLEMENTED
* 
* @author      Keren Solomon
*/
public class WifeCard extends AbstractOwnerableCard {

	public WifeCard(CardEntity cEntity, int gameId) {
		super(cEntity.getId(),
				gameId,
				cEntity.getName(),
				cEntity.getDecore().getTxtCol(),
				cEntity.getDecore().getImg(),
				cEntity.getDecore().getFrameImg(),
				cEntity.getDecore().getBackImg());
	}
	
	@Override
	public Boolean isCouple(AbstractCard card) {
		return (card instanceof HusbandCard);
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
