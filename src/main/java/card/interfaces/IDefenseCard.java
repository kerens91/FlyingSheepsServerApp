package card.interfaces;

import card.AbstractCard;
import game.player.Player;

public interface IDefenseCard {
	Boolean isStealDefenseCard();
	Boolean isPitDefenseCard();
	Boolean isCliffDefenseCard();
	Boolean isAvalancheDefenseCard();
	
	void pickedDefenseCard(AbstractCard card, Player player);
}
