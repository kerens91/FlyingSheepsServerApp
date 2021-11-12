package attackstate.interfaces;

import attackmsg.AttackMsgInfo;

/**
 * 
 * Interface class that defines the methods to be implemented by whoever is using the attack states model.
 * This interface is implemented by any attack card and nature disaster cards.
 * 
 * @author      Keren Solomon
 */
public interface IAttackStatable {
	/**
	 * This method indicates whether an action is needed prior to the attack
	 */
	Boolean preAttackNeeded();
	
	/**
	 * This method deals with a case where attack occurs
	 */
	void doAttack();
	
	/**
	 * This method deals with a case where someone succeeded to attack
	 */
	void attackSucceeded();
	
	/**
	 * This method gets the relevant attack message information,
	 * based on attack state and the destination (receiver of the message).
	 */
	AttackMsgInfo getTitle(int state, int destination);
}
