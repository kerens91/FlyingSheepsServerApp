package attackstate.interfaces;

import attackmsg.AttackMsgInfo;

public interface IAttackStatable {
	public abstract Boolean preAttackNeeded();
	public abstract void doAttack();
	public abstract AttackMsgInfo getTitle(int state, int destination);
	public abstract void attackSucceeded();
}
