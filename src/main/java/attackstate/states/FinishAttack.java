package attackstate.states;

import globals.Constants;

public class FinishAttack extends AbstractAttackState {
	public FinishAttack() {
		super(Constants.CS_STATE_FIN);
	}
	
	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] finish-Attack state");
	}
	
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] finish-Attack state");

		if (getAttackRes()) {
			current = attackSucceeded;
		}
		else {
			current = attackFailed;
		}
		
		leave();
	}
	
	@Override
	public void leave() {
		current.enter();
		current.execute();
	}
}
