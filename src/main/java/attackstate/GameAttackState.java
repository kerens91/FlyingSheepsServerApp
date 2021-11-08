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
import eventnotifications.IAttackNotifications;
import game.GameManager;
import game.gameplayers.Player;

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
		if (AbstractAttackState.current == AbstractAttackState.noAttack) {
			return false;
		}
		return true;
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
		if (((IAttackDefensable) getAttackCard()).defenseSucceeded(getHelperCard())) {
			return true;
		}
		return false;
	}


	public void registerNotifications() {
		AbstractAttackState.current.registerCallback(GameManager.getInstance());
	}
}
