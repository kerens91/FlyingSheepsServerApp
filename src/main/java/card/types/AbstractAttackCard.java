package card.types;

import java.util.HashMap;
import java.util.List;

import attackmsg.AttackMsgInfo;
import attackstate.interfaces.IAttackStatable;
import card.AbstractCard;
import database.entity.StringEntity;
import game.player.Player;
import globals.Constants;


public abstract class AbstractAttackCard extends AbstractValueableCard implements IAttackStatable {
	private HashMap<Integer,HashMap<Integer,AttackMsgInfo>> cardStrings;
	
	public AbstractAttackCard(int typeId, int gameId, String name, int txtCol, String img, String frame, String back, int value, String points, List<StringEntity> strings) {
		super(typeId, gameId, name, txtCol, img, frame, back, value, points);
		cardStrings = initCardsStringsMap(strings);
	}
		
	public void pickedAttackCard(AbstractCard card, Player player) {
		cardNotifications.attackCardPicked(card, player);
	}

	@Override
	public AttackMsgInfo getTitle(int state, int destination) {
		return cardStrings.get(state).get(destination);
	}
	
	public HashMap<Integer,HashMap<Integer,AttackMsgInfo>> initCardsStringsMap(List<StringEntity> strings) {
		HashMap<Integer, HashMap<Integer, AttackMsgInfo>> stateToStringMap = new HashMap<>();
		HashMap<Integer,AttackMsgInfo> destToStringMap;
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
			
			System.out.println("type " + info.getType() + ", state = " + state + ", pre = " + info.getPre() + ", " + info.getString() + ", post = " + info.getPost());
		}
		
		return stateToStringMap;
	}
}
