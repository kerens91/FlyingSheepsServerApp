package card.interfaces;

import card.AbstractCard;
import game.player.Player;

public interface ICoupledCard {
	public abstract Boolean isCouple(AbstractCard card);
	public abstract void doCouple(Player player);
}
