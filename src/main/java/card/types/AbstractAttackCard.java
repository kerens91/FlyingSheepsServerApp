package card.types;

import java.util.List;
import java.util.Map;

import attackmsg.AttackMsgInfo;
import attackstate.interfaces.IAttackStatable;
import card.AbstractCard;
import database.entity.StringEntity;
import game.gameplayers.Player;


public abstract class AbstractAttackCard extends AbstractValueableCard implements IAttackStatable {
	private Map<Integer,Map<Integer,AttackMsgInfo>> cardStrings;
	
	public AbstractAttackCard(int typeId, int gameId, String name, int txtCol, String img, String frame, String back, int value, String points, List<StringEntity> strings) {
		super(typeId, gameId, name, txtCol, img, frame, back, value, points);
		cardStrings = initCardsStrings(strings);
	}
		
	public void pickedAttackCard(AbstractCard card, Player player) {
		cardNotifications.attackCardPicked(card, player);
	}

	@Override
	public AttackMsgInfo getTitle(int state, int destination) {
		return cardStrings.get(state).get(destination);
	}
	
}
