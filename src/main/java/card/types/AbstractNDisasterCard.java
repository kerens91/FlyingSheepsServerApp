package card.types;

import java.util.List;
import java.util.Map;

import attackmsg.AttackMsgInfo;
import attackstate.interfaces.IAttackDefensable;
import attackstate.interfaces.IAttackStatable;
import card.AbstractCard;
import clientservershared.CardModel;
import database.entity.StringEntity;
import game.gameplayers.Player;
import globals.Constants;



public abstract class AbstractNDisasterCard extends AbstractCard implements IAttackStatable, IAttackDefensable {
private Map<Integer,Map<Integer,AttackMsgInfo>> cardStrings;
private Map<Integer,Map<Integer,AttackMsgInfo>> cardStrings2;
	
	public AbstractNDisasterCard(int typeId, int gameId, String name, String img, List<StringEntity> strings) {
		super(typeId, gameId, name, img);
		cardStrings = initCardsStrings(strings);
	}
	
	public Boolean playCardFromDeck(Player player) {
		natureDisasterAttack(player);
		return Constants.CARD_DONT_END_TURN;	
	}
	
	public void natureDisasterAttack(Player player) {
		cardNotifications.startNatureDisasterAttack(this, player);
	}

	@Override
	public Boolean preAttackNeeded() {
		return false;
	}
	
	@Override
	public void doAttack() {
		cardNotifications.notifyAttackOnPlayer();
	}
	
	@Override
	public void attackSucceeded() {
		cardNotifications.natureDisasterAttackSucceeded();
	}

	@Override
	public void attackFailed() {
		cardNotifications.natureDisasterAttackFailed();
	}
	
	@Override
	public AttackMsgInfo getTitle(int state, int destination) {
		return cardStrings.get(state).get(destination);
	}
	
	@Override
		public Boolean isPlayable() {
			return false;
		}
	
	@Override
		public CardModel getCardInfo() {
			return new CardModel(getId(), getName(), getImg(), 0, null, null, null);
		}
}
