package card.interfaces;

import card.AbstractCard;
import game.player.Player;

public interface IDefenseCard {
	public Boolean isStealDefenseCard();
	public Boolean isPitDefenseCard();
	public Boolean isCliffDefenseCard();
	public Boolean isAvalancheDefenseCard();
	
	public abstract void pickedDefenseCard(AbstractCard card, Player player);
}
