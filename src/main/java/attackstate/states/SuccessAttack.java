package attackstate.states;

import attackstate.interfaces.IAttackStatable;
import globals.Constants;

public class SuccessAttack extends AbstractAttackState {
	public SuccessAttack() {
		super(Constants.CS_STATE_SUC);
	}

	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] Attack Succeeded state");
	}
	
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] Attack Succeeded state");
		((IAttackStatable)attackCard).attackSucceeded();
	}

	@Override
	public void leave() {
		current = noAttack;
		current.enter();
	}
}
