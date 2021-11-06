package card.implementation.special;

import static card.types.AbstractOwnerableCard.cardFeatureType.FEATURE_TYPE_WIN;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import game.gameplayers.Player;

public class BombCard extends AbstractOwnerableCard {
	public BombCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back) {
		super(typeId, gameId, name, txtColor, img, frame, back);
	}

	@Override
	public Boolean isCouple(AbstractCard card) {
		if (card instanceof SuperFlyingCard) {
			return true;
		}
		return false;
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
