package card.interfaces;

import card.AbstractCard;
import game.gameplayers.Player;

public interface ICoupledCard {
	Boolean isCouple(AbstractCard card);
	void doCouple(Player player);
}
