package card.implementation.regular;

import java.util.List;

import attackstate.interfaces.IAttackDefensable;
import card.AbstractCard;
import card.interfaces.IDefenseCard;
import card.types.AbstractAttackCard;
import database.entity.StringEntity;
import game.player.Player;


public class StealCard extends AbstractAttackCard implements IAttackDefensable {
	public StealCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back, int value, String points, List<StringEntity> strings) {
		super(typeId, gameId, name, txtColor, img, frame, back, value, points, strings);
	}
	
	@Override
	public Boolean preAttackNeeded() {
		return true;
	}

	@Override
	public void doAttack() {
		cardNotifications.notifyStealAttack();
	}

	@Override
	public void attackSucceeded() {
		cardNotifications.stealAttackSucceeded();
	}
	
	@Override
	public void attackFailed() {
		cardNotifications.stealAttackFailed();
	}

	@Override
	public void playerPickedCard(Player player) {
		setCardUsed(player);
	}

	@Override
	public Boolean defenseSucceeded(AbstractCard defenseCard) {
		if (((IDefenseCard) defenseCard).isStealDefenseCard()) {
			return true;
		}
		return false;
	}
}
