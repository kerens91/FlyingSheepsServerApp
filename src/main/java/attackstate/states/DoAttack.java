package attackstate.states;

import static globals.Constants.CS_STATE_DO;

import attackstate.interfaces.IAttackStatable;

/**
 * 
 * This class defines the Do-Attack state.
 * Next state will be finish-attack.
 * 
 * @author      Keren Solomon
 */
public class DoAttack extends AbstractAttackState {
	public DoAttack() {
		super(CS_STATE_DO);
	}
	
	/**
	 * When entering the Do-Attack state, debug is printed to logger.
	 */
	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] Do-Attack state");
	}
	
	/**
	 * When executing the Do-Attack state, the attack card is used and the attack occurs.
	 */
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] Do-Attack state");
		((IAttackStatable)attackCard).doAttack();
	}

	/**
	 * When leaving the Do-Attack state, enter the next finish attack state.
	 */
	@Override
	public void leave() {
		current = finishAttack;
		current.enter();
	}
}
