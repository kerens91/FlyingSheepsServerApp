package card.interfaces;

import card.AbstractCard;
import game.gameplayers.Player;

/**
 * 
 * Interface class that defines the methods to be implemented by whoever is using the Couple functionality.
 * This interface is implemented by the regular cards and the special cards.
 * 
 * @author      Keren Solomon
 */
public interface ICoupledCard {
	/**
	 * This method indicates that 2 picked cards are considered as a couple.
	 */
	Boolean isCouple(AbstractCard card);
	
	/**
	 * This method is called in case 2 picked cards are matching couple.
	 */
	void doCouple(Player player);
}
