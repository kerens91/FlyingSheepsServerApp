package eventnotifications;

import card.AbstractCard;
import game.player.Player;

public interface ICardNotifications {
	public void onGotFromDeckBomb(int cardId);
	public void onGotFromDeckSuper(int cardId);
	public void onGotFromDeckHusband(int cardId);
	public void onGotFromDeckWife(int cardId);
	
	public void attackCardPicked(AbstractCard card, Player player);
	public void defenseCardPicked(AbstractCard card, Player player);
	public void defenseCardPickedFlyingSheep(AbstractCard card, Player player);
	public void startNatureDisasterAttack(AbstractCard card, Player player);
	
	public void doRockAttack();
	public void rockAttackSucceeded();
	
	public void notifyStealAttack();
	public void stealAttackSucceeded();
	public void stealAttackFailed();
	
	public void doRiverAttack();
	public void riverAttackSucceeded();
	
	public void doTreeAttack();
	public void treeAttackSucceeded();
	public void treeAttackFailed();
	
	public void notifyNatureDisasterAttack();
	public void natureDisasterAttackSucceeded();
	public void natureDisasterAttackFailed();
	
	public void specialCoupleWinGame(Player player);
	public void specialCoupleShowCoopBtn();
}
