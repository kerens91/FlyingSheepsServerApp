package card.implementation.attack;

import java.util.List;

import card.types.AbstractAttackCard;
import database.entity.StringEntity;
import game.player.Player;



public class RockCard extends AbstractAttackCard {
	public RockCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back, int val, String points, List<StringEntity> strings) {
		super(typeId, gameId, name, txtColor, img, frame, back, val, points, strings);
	}
	
	
	@Override
	public void playerPickedCard(Player player) {
		setCardUsed(player);
		pickedAttackCard(this, player);
	}
	

	@Override
	public Boolean preAttackNeeded() {
		return true;
	}

	@Override
	public void doAttack() {
		cardNotifications.doRockAttack();
	}
	
	@Override
	public void attackSucceeded() {
		cardNotifications.rockAttackSucceeded();
	}
}
