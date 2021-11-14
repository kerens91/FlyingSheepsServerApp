package attackstate.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import attackmsg.AttackMsgInfo;
import attackstate.interfaces.IAttackStatable;
import card.AbstractCard;
import eventnotifications.IAttackNotifications;
import game.players.Player;
import globals.Configs;

/**
 * 
 * Abstract class that defines an attack-state object.
 * Attack state can represent one of the following states:
 * <ul>
 * <li>noAttack
 * <li>preAttack
 * <li>doAttack
 * <li>finishAttack
 * <li>attackSucceeded
 * <li>attackFailed
 * </ul>
 * 
 * Each attack state will have to implement three methods:
 * <ul>
 * <li>enter - called when entering the state
 * <li>execute - called by the AttackHandler when action is required
 * <li>leave - called by the AttackHandler or by the state itself when finished execution
 * </ul>
 * Each attack state will have an id.
 * All attack states share the following attack information:
 * <ul>
 * <li>attacker - the player that started the attack
 * <li>victim - the player that is a victim to an attack
 * <li>attackCard - the card that started the attack
 * <li>helperCard - the card defended from the attack / the lost card as a result of the attack
 * </ul>
 * 
 * the current member represent the current active state. it is initiated as 'no attack' state.
 * once attack occurs during the game the state changes accordingly.
 * 
 * @author      Keren Solomon
 */
public abstract class AbstractAttackState {
	static final Logger logger = LogManager.getLogger(AbstractAttackState.class);
	static Configs configs = Configs.getInstance();
	static Player attacker;
	static Player victim;
	static AbstractCard attackCard;
	static AbstractCard helperCard;
	static Boolean attackResult;
	static IAttackNotifications notifications;
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
