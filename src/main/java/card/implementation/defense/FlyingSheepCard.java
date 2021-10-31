package card.implementation.defense;

import card.AbstractCard;
import card.interfaces.IDefenseCard;
import card.types.AbstractValueableCard;
import game.player.Player;

public class FlyingSheepCard extends AbstractValueableCard implements IDefenseCard {
	public FlyingSheepCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back, int value, String points) {
		super(typeId, gameId, name, txtColor, img, frame, back, value, points);
	}

	
	@Override
	public void playerPickedCard(Player player) {
		pickedDefenseCard(this, player);
	}
	
	@Override
	public void pickedDefenseCard(AbstractCard card, Player player) {
		cardNotifications.defenseCardPickedFlyingSheep(card, player);
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
		return false;
	}

}
