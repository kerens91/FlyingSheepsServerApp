package attackstate.states;

import static globals.Constants.CS_STATE_FIN;

/**
 * 
 * This class defines the Finish-Attack state.
 * This state is the point decide whether the attack succeeded or failed.
 * Next state will be attack-succeeded / attack failed.
 * 
 * @author      Keren Solomon
 */
public class FinishAttack extends AbstractAttackState {
	public FinishAttack() {
		super(CS_STATE_FIN);
	}
	
	/**
	 * When entering the Finish-Attack state, debug is printed to logger.
	 */
	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] finish-Attack state");
	}
	
	/**
	 * When executing the Finish-Attack state,
	 * check the attack result for success or failure,
	 * call leave state.
	 */
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] finish-Attack state");

		setAttackResult();
		leave();
	}
	
	/**
	 * When leaving the Finish-Attack state, enter and execute the next state.
	 */
	@Override
	public void leave() {
		current.enter();
		current.execute();
	}
	
	private void setAttackResult() {
		if (getAttackRes()) {
			current = attackSucceeded;
		}
		else {
			current = attackFailed;
		}
	}
}
