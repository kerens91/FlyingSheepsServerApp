package eventnotifications;

import card.AbstractCard;
import game.player.Player;

public interface ICardNotifications {
	void onGotFromDeckBomb(int cardId);
	void onGotFromDeckSuper(int cardId);
	void onGotFromDeckHusband(int cardId);
	void onGotFromDeckWife(int cardId);
	
	void attackCardPicked(AbstractCard card, Player player);
	void defenseCardPicked(AbstractCard card, Player player);
	void defenseCardPickedFlyingSheep(AbstractCard card, Player player);
	void startNatureDisasterAttack(AbstractCard card, Player player);
	
	void doRockAttack();
	void rockAttackSucceeded();
	
	void notifyStealAttack();
	void stealAttackSucceeded();
	void stealAttackFailed();
	
	void doRiverAttack();
	void riverAttackSucceeded();
	
	void doTreeAttack();
	void treeAttackSucceeded();
	void treeAttackFailed();
	
	void notifyNatureDisasterAttack();
	void natureDisasterAttackSucceeded();
	void natureDisasterAttackFailed();
	
	void specialCoupleWinGame(Player player);
	void specialCoupleShowCoopBtn();
}
