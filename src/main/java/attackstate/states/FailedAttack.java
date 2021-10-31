package attackstate.states;

import attackstate.interfaces.IAttackFailable;
import globals.Constants;

public class FailedAttack extends AbstractAttackState {
	public FailedAttack() {
		super(Constants.CS_STATE_FAIL);
	}

	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] Attack Failed state");
	}
	
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] Attack Failed state");
		((IAttackFailable)attackCard).attackFailed();
		
	}

	@Override
	public void leave() {
		current = noAttack;
		current.enter();
	}
}
