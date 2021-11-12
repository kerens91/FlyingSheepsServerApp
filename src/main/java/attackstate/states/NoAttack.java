package attackstate.states;

import static globals.Constants.CS_STATE_NO;

import attackstate.interfaces.IAttackStatable;

/**
 * 
 * This class defines the No-Attack state.
 * This is the default state when the game starts.
 * Next state will be pre-attack / do-attack.
 * 
 * @author      Keren Solomon
 */
public class NoAttack extends AbstractAttackState {
	public NoAttack() {
		super(CS_STATE_NO);
	}

	/**
	 * When entering the No-Attack state, all attack values are initiated to null.
	 */
	@Override
	public void enter() {
		logger.info("Entering attack state [" + getStateId() + "] No-Attack state");
		initAttackValues();
	}
	
	/**
	 * When executing the No-Attack state,
	 * check if pre-attack action is needed to decide which is the next state,
	 * call leave state.
	 */
	@Override
	public void execute() {
		logger.info("Executing attack state [" + getStateId() + "] No-Attack state");
		setStartState();
		leave();
	}
	
	/**
	 * When leaving the No-Attack state, enter and execute the next state.
	 */
	@Override
	public void leave() {
		current.enter();
		current.execute();
	}
	
	private void initAttackValues() {
		attackCard = null;
		attacker = null;
		victim = null;
		helperCard = null;
		attackResult = false;
	}
	
	private void setStartState() {
		if (((IAttackStatable) attackCard).preAttackNeeded()) {
			current = preAttack;
		}
		else {
			current = doAttack;
		}
	}

}
