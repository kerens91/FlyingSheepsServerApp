package attackstate.interfaces;

/**
 * 
 * Interface class that has a single method to implement.
 * Whoever implement this interface is obligated to handle the case in which an attack failed.
 * This interface is implemented by steal card, tree card and nature disaster cards.
 * 
 * @author      Keren Solomon
 */
public interface IAttackFailable {
	/**
	 * This method deals with a case where someone tried to attack but failed in the attack
	 */
	void attackFailed();
}
