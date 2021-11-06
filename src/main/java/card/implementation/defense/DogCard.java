package card.implementation.defense;

import card.AbstractCard;
import card.interfaces.IDefenseCard;
import card.types.AbstractPlayableCard;
import game.gameplayers.Player;


public class DogCard extends AbstractPlayableCard implements IDefenseCard {
	public DogCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back) {
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
		return true;
	}

	@Override
	public Boolean isPitDefenseCard() {
		return false;
	}

	@Override
	public Boolean isCliffDefenseCard() {
		return false;
	}

	@Override
	public Boolean isAvalancheDefenseCard() {
		return false;
	}
}
