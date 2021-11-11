package card.types;

import java.util.HashMap;
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
	
	public Map<Integer,Map<Integer,AttackMsgInfo>> initCardsStrings(List<StringEntity> strings) {
		Map<Integer, Map<Integer, AttackMsgInfo>> stateToStringMap = new HashMap<>();
		Map<Integer,AttackMsgInfo> destToStringMap;
		AttackMsgInfo msg;
		int state, destination;
		
		System.out.println("strings for card " + getName() + ":");
		
		for (StringEntity info : strings) {
			state = info.getState();
			destination = info.getDest();
			msg = new AttackMsgInfo(info.getType(), info.getPre(), info.getString(), info.getPost());
			
			if (stateToStringMap.containsKey(state)) {
				destToStringMap = stateToStringMap.get(state);
				
				if (destToStringMap.containsKey(destination)) {
					System.out.println("msg already exists");
				}
				else {
					destToStringMap.put(destination, msg);
					stateToStringMap.replace(state, destToStringMap);
				}
			}
			else {
				destToStringMap = new HashMap<>(Constants.DEST_NUM);
				destToStringMap.put(destination, msg);
				stateToStringMap.put(state, destToStringMap);
			}
			
			System.out.println("type " + info.getType() + ", pre = " + info.getPre() + ", " + info.getString() + ", post = " + info.getPost());
		}
		
		return stateToStringMap;
	}
	
	@Override
		public CardModel getCardInfo() {
			return new CardModel(getId(), getName(), getImg(), 0, null, null, null);
		}
}
