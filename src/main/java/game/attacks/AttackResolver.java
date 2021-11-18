package game.attacks;

import static globals.Constants.*;

import java.util.HashMap;
import java.util.Map;

import game.players.PlayersManager;

/**
* This class represents the attack resolver, that is responsible for handling the results of the attacks.
* 
* Attack can have two types of results:
* <ul>
* <li>AttackSucceeded
* <li>AttackFailed
* </ul>
* 
* The class updates the attack state, calls the handling method if needed and gets the generated message if needed.
* The class is created by the Game class, and is used by the game manager class.
* 
* @author      Keren Solomon
*/
public class AttackResolver {
	private AttackHandler attackHandler;
	private PlayersManager playersManager;
	private AttackMsgGenerator msgGenerator;
	
	/**
	 * Creates an attack resolver to handle the attack results.
	 * The AttackResolver class is created with an attack handler member.
	 * the players manager and the message generator members are initiated with values given from the attack handler.
	 * 
	 * It is created by the Game class.
	 * 
	 * @param attackHandler    	represents the attack handler member.
	 */
	public AttackResolver(AttackHandler attackHandler) {
		this.attackHandler = attackHandler;
		this.playersManager = attackHandler.getPlayersManager();
		this.msgGenerator = attackHandler.getMsgGenerator();
	}
	
	public Map<Integer, AttackMsgWrapper> rockAttackSucceeded() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		String victim = attackHandler.getVictim();
		destToMsgMap.put(DEST_VICTIM, new AttackMsgWrapper(victim, msgGenerator.getMsgVicRockAttSuc()));
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(victim), msgGenerator.getMsgAllRockAttSuc()));
		
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> stealAttackSucceeded() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		String victim = attackHandler.getVictim();
		String attacker = attackHandler.getAttacker();
		destToMsgMap.put(DEST_VICTIM, new AttackMsgWrapper(victim, msgGenerator.getMsgVicStealAttSuc()));
		destToMsgMap.put(DEST_ATTACKER, new AttackMsgWrapper(attacker, msgGenerator.getMsgAttStealAttSuc()));
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(victim, attacker), msgGenerator.getMsgAllStealAttSuc()));

		attackHandler.nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> stealAttackFailed() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(), msgGenerator.getMsgAllStealAttFailed()));
				
		attackHandler.nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> riverAttackSucceeded() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);

		String attacker = attackHandler.getAttacker();
		destToMsgMap.put(DEST_ATTACKER, new AttackMsgWrapper(attacker, msgGenerator.getMsgAllRiverAttSuc()));
		
		attackHandler.nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> treeAttackSucceeded() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		String attacker = attackHandler.getAttacker();
		destToMsgMap.put(DEST_ATTACKER, new AttackMsgWrapper(attacker, msgGenerator.getMsgTreeAttSuc(attackHandler.getSpecialCardsOwners())));
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(attacker), msgGenerator.getMsgAllTreeAtt()));

		attackHandler.nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> treeAttackFailed() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);

		String attacker = attackHandler.getAttacker();
		destToMsgMap.put(DEST_ATTACKER, new AttackMsgWrapper(attacker, msgGenerator.getMsgTreeAttFail()));
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(attacker), msgGenerator.getMsgAllTreeAtt()));
		
		attackHandler.nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> natureDisasterAttackSucceeded() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(), msgGenerator.getMsgAllNatureAttSuc()));

		attackHandler.nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> natureDisasterAttackFailed() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);

		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(), msgGenerator.getMsgAllNatureAttFail()));

		attackHandler.nextAttackState();
		return destToMsgMap;
	}
}
