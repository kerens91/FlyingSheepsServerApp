package card.implementation.special;

import static card.types.AbstractOwnerableCard.cardFeatureType.FEATURE_TYPE_COOP;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import game.gameplayers.Player;

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
