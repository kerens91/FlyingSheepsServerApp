package attackstate.states;

import attackstate.interfaces.IAttackStatable;
import globals.Constants;

public class DoAttack extends AbstractAttackState {
	public DoAttack() {
		super(Constants.CS_STATE_DO);
	}
	
	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] Do-Attack state");
	}
	
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] Do-Attack state");
		((IAttackStatable)attackCard).doAttack();
	}


	@Override
	public void leave() {
		current = finishAttack;
		current.enter();
	}
}
