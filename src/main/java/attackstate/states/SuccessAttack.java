package attackstate.states;

import static globals.Constants.CS_STATE_SUC;

import attackstate.interfaces.IAttackStatable;

/**
 * 
 * This class defines the Success-Attack state.
 * Next state will finish the attack with success result,
 * and after that move again to no-attack state.
 * 
 * @author      Keren Solomon
 */
public class SuccessAttack extends AbstractAttackState {
	public SuccessAttack() {
		super(CS_STATE_SUC);
	}

	/**
	 * When entering the Success-Attack state, debug is printed to logger.
	 */
	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] Attack Succeeded state");
	}
	
	/**
	 * When executing the Success-Attack state,
	 * use the attack card to complete the attack, call attack succeeded method.
	 */
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] Attack Succeeded state");
		((IAttackStatable)attackCard).attackSucceeded();
	}

	/**
	 * When leaving the Success-Attack state, enter the No-attack state.
	 */
	@Override
	public void leave() {
		current = noAttack;
		current.enter();
	}
}
