package attackstate.states;

import attackstate.interfaces.IAttackStatable;
import globals.Constants;

public class NoAttack extends AbstractAttackState {
	public NoAttack() {
		super(Constants.CS_STATE_NO);
	}

	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] No-Attack state");
		attackCard = null;
		attacker = null;
		victim = null;
		helperCard = null;
		attackResult = false;
	}
	
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] No-Attack state");
		if (((IAttackStatable) attackCard).preAttackNeeded()) {
			current = preAttack;
		}
		else {
			current = doAttack;
		}
		
		leave();
	}
	
	@Override
	public void leave() {
		current.enter();
		current.execute();
	}

}
