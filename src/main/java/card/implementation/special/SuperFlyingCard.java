package card.implementation.special;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import game.player.Player;

public class SuperFlyingCard extends AbstractOwnerableCard {
	public SuperFlyingCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back) {
		super(typeId, gameId, name, txtColor, img, frame, back);
	}

	
	public Boolean playCardFromDeck(Player player) {
		Boolean res = super.playCardFromDeck(player);
		cardNotifications.onGotFromDeckSuper(getId());
		return res;
	}

	@Override
	public Boolean isCouple(AbstractCard card) {
		if (card instanceof BombCard) {
			return true;
		}
		return false;
	}
	
	@Override
	public void doCouple(Player player) {
		cardNotifications.specialCoupleWinGame(player);
	}
}
