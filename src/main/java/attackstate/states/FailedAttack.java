package attackstate.states;

import static globals.Constants.CS_STATE_FAIL;

import attackstate.interfaces.IAttackFailable;

/**
 * 
 * This class defines the Failed-Attack state.
 * Next state will finish the attack with fail result,
 * and after that move again to no-attack state.
 * 
 * @author      Keren Solomon
 */
public class FailedAttack extends AbstractAttackState {
	public FailedAttack() {
		super(CS_STATE_FAIL);
	}

	/**
	 * When entering the Failed-Attack state, debug is printed to logger.
	 */
	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] Attack Failed state");
	}
	
	/**
	 * When executing the Failed-Attack state,
	 * use the attack card to complete the attack, call attack failed method.
	 */
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] Attack Failed state");
		((IAttackFailable)attackCard).attackFailed();
		
	}

	/**
	 * When leaving the Failed-Attack state, enter the No-attack state.
	 */
	@Override
	public void leave() {
		current = noAttack;
		current.enter();
	}
}
