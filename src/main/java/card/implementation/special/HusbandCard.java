package card.implementation.special;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import game.player.Player;

public class HusbandCard extends AbstractOwnerableCard {
	public HusbandCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back) {
		super(typeId, gameId, name, txtColor, img, frame, back);
	}

	
	public Boolean playCardFromDeck(Player player) {
		Boolean res = super.playCardFromDeck(player);
		cardNotifications.onGotFromDeckHusband(getId());
		return res;
	}

	@Override
	public Boolean isCouple(AbstractCard card) {
		if (card instanceof WifeCard) {
			return true;
		}
		return false;
	}
	
	@Override
	public void doCouple(Player player) {
		cardNotifications.specialCoupleShowCoopBtn();
	}

}
