package card.implementation.defense;

import card.AbstractCard;
import card.interfaces.IDefenseCard;
import card.types.AbstractPlayableCard;
import game.player.Player;

public class StickCard extends AbstractPlayableCard implements IDefenseCard {
	public StickCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back) {
		super(typeId, gameId, name, txtColor, img, frame, back);
	}

	@Override
	public void playerPickedCard(Player player) {
		setCardUsed(player);
		pickedDefenseCard(this, player);
	}
	
	@Override
	public void pickedDefenseCard(AbstractCard card, Player player) {
		cardNotifications.defenseCardPicked(card, player);
	}
	
	@Override
	public Boolean isStealDefenseCard() {
		return false;
	}

	@Override
	public Boolean isPitDefenseCard() {
		return true;
	}

	@Override
	public Boolean isCliffDefenseCard() {
		return true;
	}

	@Override
	public Boolean isAvalancheDefenseCard() {
		return true;
	}

}
