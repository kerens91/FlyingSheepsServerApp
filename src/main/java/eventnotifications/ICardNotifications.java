package eventnotifications;

import card.AbstractCard;
import game.players.Player;

public interface ICardNotifications {
	void onGotFromDeckSpecial(int cardId);
	
	void attackCardPicked(AbstractCard card, Player player);
	void defenseCardPicked(AbstractCard card, Player player);
	void defenseCardPickedFlyingSheep(AbstractCard card, Player player);
	void startNatureDisasterAttack(AbstractCard card, Player player);
	
	void notifyAttackOnPlayer();
	
	void doRockAttack();
	void rockAttackSucceeded();
	
	void stealAttackSucceeded();
	void stealAttackFailed();
	
	void doRiverAttack();
	void riverAttackSucceeded();
	
	void doTreeAttack();
	void treeAttackSucceeded();
	void treeAttackFailed();
	
	void natureDisasterAttackSucceeded();
	void natureDisasterAttackFailed();
	
	void specialCoupleWinGame(Player player);
	void specialCoupleShowCoopBtn();
}
