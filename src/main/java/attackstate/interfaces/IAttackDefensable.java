package attackstate.interfaces;

import card.AbstractCard;

/**
 * 
 * Interface class that has a single method to implement.
 * Whoever implement this interface is obligated to handle an attack defense attempt.
 * This interface inherits from IAttackFailable interface.
 * This interface is implemented by steal card and nature disaster cards.
 * 
 * @author      Keren Solomon
 */
public interface IAttackDefensable extends IAttackFailable {
	/**
	 * This method indicates that the defense against an attack was successful or failed.
	 */
	Boolean defenseSucceeded(AbstractCard defenseCard);
}
