package attackstate.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import attackmsg.AttackMsgInfo;
import attackstate.interfaces.IAttackStatable;
import card.AbstractCard;
import eventnotifications.IAttackNotifications;
import game.gameplayers.Player;
import globals.Configs;

public abstract class AbstractAttackState {
	protected static final Logger logger = LogManager.getLogger(AbstractAttackState.class);
	protected static Configs configs = Configs.getInstance();
	protected static Player attacker;
	protected static Player victim;
	protected static AbstractCard attackCard;
	protected static AbstractCard helperCard;
	protected static Boolean attackResult;
	protected static IAttackNotifications notifications;
	private int stateId;
	
	public AbstractAttackState(int stateId) {
		this.stateId = stateId;
	}
	
	public static AbstractAttackState noAttack, preAttack, doAttack, finishAttack, attackSucceeded, attackFailed, current;
	
	public abstract void enter();
	public abstract void execute();
	public abstract void leave();
	
	public AttackMsgInfo getMsgInfo(int destination) {
		return ((IAttackStatable) attackCard).getTitle(getStateId(), destination);
	}
	
	public int getStateId() {
		return stateId;
	}
	public void registerCallback (IAttackNotifications attackNotifications) {
		notifications = attackNotifications;
	}
	
	public Boolean getAttackRes() {
		return attackResult;
	}
	
	public void setAttackRes(Boolean attackSucceeded) {
		attackResult = attackSucceeded;
	}
	
	public Player getAttacker() {
		return attacker;
	}

	public void setAttacker(Player player) {
		attacker = player;
	}

	public Player getVictim() {
		return victim;
	}

	public void setVictim(Player player) {
		victim = player;
	}

	public AbstractCard getAttackCard() {
		return attackCard;
	}

	public void setAttackCard(AbstractCard card) {
		attackCard = card;
	}

	public AbstractCard getHelperCard() {
		return helperCard;
	}

	public void setHelperCard(AbstractCard defenseCard) {
		helperCard = defenseCard;
	}
}
