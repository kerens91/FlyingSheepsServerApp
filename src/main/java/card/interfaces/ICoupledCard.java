package card.interfaces;

import card.AbstractCard;
import game.player.Player;

public interface ICoupledCard {
	Boolean isCouple(AbstractCard card);
	void doCouple(Player player);
}
