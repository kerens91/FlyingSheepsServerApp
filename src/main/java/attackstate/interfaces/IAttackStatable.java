package attackstate.interfaces;

import attackmsg.AttackMsgInfo;

public interface IAttackStatable {
	Boolean preAttackNeeded();
	void doAttack();
	AttackMsgInfo getTitle(int state, int destination);
	void attackSucceeded();
}
