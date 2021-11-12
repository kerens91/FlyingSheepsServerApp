package attackstate.states;

import static globals.Constants.CS_STATE_PRE;

/**
 * 
 * This class defines the Pre-Attack state.
 * This is optional state, only when needed.
 * Next state will be do-attack.
 * 
 * @author      Keren Solomon
 */
public class PreAttack extends AbstractAttackState {
	public PreAttack() {
		super(CS_STATE_PRE);
	}

	/**
	 * When entering the Pre-Attack state, debug is printed to logger.
	 */
	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] Pre-Attack state");
	}
	
	/**
	 * When executing the Pre-Attack state,
	 * an event is sent to the GameManager in order to get the victim of the attack.
	 */
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] Pre-Attack state");
		notifications.askVictimForAttack();
	}
	
	/**
	 * When leaving the Pre-Attack state, enter the do-attack state.
	 */
	@Override
	public void leave() {
		current = doAttack;
		current.enter();
	}
}
