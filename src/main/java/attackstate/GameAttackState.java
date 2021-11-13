package attackstate;

import attackstate.interfaces.IAttackDefensable;
import attackstate.states.AbstractAttackState;
import attackstate.states.DoAttack;
import attackstate.states.FailedAttack;
import attackstate.states.FinishAttack;
import attackstate.states.NoAttack;
import attackstate.states.PreAttack;
import attackstate.states.SuccessAttack;
import card.AbstractCard;
import game.GameManager;
import game.players.Player;

/**
* This class manages the game attack states.
* It is responsible for initiating the different states classes.
* It initiate current state as No-attack state.
* 
* This class defines APIs for accessing the attack states,
* the current state and the attack information.
* This APIs are called from AttackHandler class during the game.
* 
* @author      Keren Solomon
*/
public class GameAttackState {
	public GameAttackState() {
		AbstractAttackState.noAttack = new NoAttack();
		AbstractAttackState.preAttack = new PreAttack();
		AbstractAttackState.doAttack = new DoAttack();
		AbstractAttackState.finishAttack = new FinishAttack();
		AbstractAttackState.attackSucceeded = new SuccessAttack();
		AbstractAttackState.attackFailed = new FailedAttack();
		AbstractAttackState.current = AbstractAttackState.noAttack;
		AbstractAttackState.current.enter();
	}
	
	
	public Boolean isAttackActive() {
		return !(AbstractAttackState.current == AbstractAttackState.noAttack);
	}
	
	public AbstractAttackState getState() {
		return AbstractAttackState.current;
	}

	public Player getAttacker() {
		return AbstractAttackState.current.getAttacker();
	}
	
	public void setAttackResult(Boolean res) {
		AbstractAttackState.current.setAttackRes(res);
	}
	
	public Boolean getAttackResult() {
		return AbstractAttackState.current.getAttackRes();
	}

	public void setAttacker(Player attacker) {
		AbstractAttackState.current.setAttacker(attacker);
	}

	public Player getVictim() {
		return AbstractAttackState.current.getVictim();
	}

	public void setVictim(Player victim) {
		AbstractAttackState.current.setVictim(victim);
	}

	public AbstractCard getAttackCard() {
		return AbstractAttackState.current.getAttackCard();
	}

	public void setAttackCard(AbstractCard attackCard) {
		AbstractAttackState.current.setAttackCard(attackCard);
	}

	public AbstractCard getHelperCard() {
		return AbstractAttackState.current.getHelperCard();
	}

	public void setHelperCard(AbstractCard defenseCard) {
		AbstractAttackState.current.setHelperCard(defenseCard);
	}
	
	public Boolean victimDefended() {
		return (((IAttackDefensable) getAttackCard()).defenseSucceeded(getHelperCard()));
	}


	public void registerNotifications() {
		AbstractAttackState.current.registerCallback(GameManager.getInstance());
	}
}
