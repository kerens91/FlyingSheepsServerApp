package card.interfaces;

import card.AbstractCard;
import game.gameplayers.Player;

public interface IDefenseCard {
	Boolean isStealDefenseCard();
	Boolean isPitDefenseCard();
	Boolean isCliffDefenseCard();
	Boolean isAvalancheDefenseCard();
	
	void pickedDefenseCard(Player player);
}
