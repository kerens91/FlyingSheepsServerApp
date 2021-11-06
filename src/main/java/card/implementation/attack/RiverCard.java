package card.implementation.attack;

import java.util.List;

import card.types.AbstractAttackCard;
import database.entity.StringEntity;
import game.gameplayers.Player;


public class RiverCard extends AbstractAttackCard {
	public RiverCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back, int val, String points, List<StringEntity> strings) {
		super(typeId, gameId, name, txtColor, img, frame, back, val, points, strings);
	}
	
	@Override
	public void playerPickedCard(Player player) {
		setCardUsed(player);
		pickedAttackCard(this, player);
	}


	@Override
	public Boolean preAttackNeeded() {
		return false;
	}
	

	@Override
	public void doAttack() {
		cardNotifications.doRiverAttack();
	}


	@Override
	public void attackSucceeded() {
		cardNotifications.riverAttackSucceeded();
	}
}
