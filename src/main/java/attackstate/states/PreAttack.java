package attackstate.states;

import globals.Constants;

public class PreAttack extends AbstractAttackState {
	public PreAttack() {
		super(Constants.CS_STATE_PRE);
	}

	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] Pre-Attack state");
	}
	
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] Pre-Attack state");
		notifications.askVictimForAttack();
	}
	
	@Override
	public void leave() {
		current = doAttack;
		current.enter();
	}
}
