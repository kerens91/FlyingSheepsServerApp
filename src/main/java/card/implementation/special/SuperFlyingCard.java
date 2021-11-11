package card.implementation.special;

import static card.types.AbstractOwnerableCard.cardFeatureType.FEATURE_TYPE_WIN;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import card.types.AbstractOwnerableCard.cardFeatureType;
import game.gameplayers.Player;

public class SuperFlyingCard extends AbstractOwnerableCard {
	public SuperFlyingCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back) {
		super(typeId, gameId, name, txtColor, img, frame, back);
	}

	@Override
	public Boolean isCouple(AbstractCard card) {
		return (card instanceof BombCard);
	}
	
	@Override
	public void doCouple(Player player) {
		cardNotifications.specialCoupleWinGame(player);
	}
	
	@Override
	public cardFeatureType getSpecialFeatureType() {
		return FEATURE_TYPE_WIN;
	}
}
