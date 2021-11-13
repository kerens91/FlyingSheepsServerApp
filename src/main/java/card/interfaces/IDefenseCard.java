package card.interfaces;

import game.gameplayers.Player;

/**
 * 
 * Interface class that defines the defense methods to be implemented by the defense cards.
 * This interface is implemented by the Shepherd Dog card, the Flying Sheep card and the Shepherd Stick card.
 * 
 * @author      Keren Solomon
 */
public interface IDefenseCard {
	/**
	 * This method indicates that the card can defend a player against a Steal attack.
	 */
	Boolean isStealDefenseCard();
	
	/**
	 * This method indicates that the card can defend a player against a Pit attack.
	 */
	Boolean isPitDefenseCard();
	
	/**
	 * This method indicates that the card can defend a player against a Cliff attack.
	 */
	Boolean isCliffDefenseCard();
	
	/**
	 * This method indicates that the card can defend a player against a Avalanche attack.
	 */
	Boolean isAvalancheDefenseCard();
	
	/**
	 * This method is called when a player picked defense card.
	 */
	void pickedDefenseCard(Player player);
}
