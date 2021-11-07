package game.gameattacks;

import static globals.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import attackmsg.AttackMsgInfo;
import attackstate.GameAttackState;
import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import card.types.AbstractPlayableCard;
import clientservershared.AttackMsg;
import eventnotifications.IAttackNotifications;
import game.gamecards.CardsManager;
import game.gameplayers.Player;
import game.gameplayers.PlayersManager;
import game.gameturns.TurnsLinkedList;
import globals.Constants;

public class AttackHandler {
	private static final Logger logger = LogManager.getLogger(AttackHandler.class);
	private GameAttackState attackState;
	private PlayersManager playersManager;
	private AttackMsgGenerator msgGenerator;
	private CardsManager cardsManager;
	private TurnsLinkedList turns;
		
	public AttackHandler(GameAttackState attackState, 
			PlayersManager playersManager, 
			AttackMsgGenerator msgGenerator, 
			CardsManager cardsManager, 
			TurnsLinkedList turns) {
		this.attackState = attackState;
		this.playersManager = playersManager;
		this.msgGenerator = msgGenerator;
		this.cardsManager = cardsManager;
		this.turns = turns;
	}
	
	public void registerCallback (IAttackNotifications notificationsCallback) {
    	attackState.registerNotifications(notificationsCallback);
    }
	
	public void nextAttackState() {
    	attackState.getState().leave();
    }
	
	public void executeState() {
    	attackState.getState().execute();
    }
    
    public void finishAttackSuc() {
    	logger.info("setting attack result succeeded");
    	attackState.setAttackResult(ATTACK_SUCCEEDED);
    	executeState();
    }
    
    public void finishAttackFail() {
    	logger.info("setting attack result failed");
    	attackState.setAttackResult(ATTACK_FAILED);
    	executeState();
    }
    
    public Player getPlayerVictim() {
    	return attackState.getState().getVictim();
    }
    
    public Player getPlayerAttacker() {
    	return attackState.getState().getAttacker();
    }
    
    public String getAttacker() {
    	return attackState.getAttacker().getId();
    }
 
    public String getVictim() {
    	return attackState.getVictim().getId();
    }
    
    public void setAttackCard(AbstractCard card) {
    	attackState.setAttackCard(card);
    }
    
    public void setHelperCard(AbstractCard card) {
    	attackState.setHelperCard(card);
    }
    
    public void setVictim(Player victim) {
    	attackState.setVictim(victim);
    }
    
    public void setAttacker(Player attacker) {
    	attackState.setAttacker(attacker);
    }
    
    public Boolean isPlayerVictim(Player player) {
    	if (attackState.getVictim().getId().equals(player.getId())) {
			return true;
		}
    	return false;
    }
    
    public Boolean isAttackActive() {
    	return attackState.isAttackActive();
    }
    
    public Boolean isVictimDefended() {
    	return attackState.victimDefended();
    }
    
    
    
    
    
    
    
    
    
    
    public Map<Integer, AttackMsgWrapper> notifyDefensableAttack() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		String victim = getVictim();
		destToMsgMap.put(DEST_VICTIM, new AttackMsgWrapper(victim, msgGenerator.getMsgVicAttNotify()));
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(victim), msgGenerator.getMsgAllAttNotify()));

		nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> askVictimForAttack() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		String attackerId = getAttacker();
		destToMsgMap.put(DEST_ATTACKER, new AttackMsgWrapper(attackerId, msgGenerator.getMsgPreAttack(playersManager.getActivePlayersIds(attackerId))));

		nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> rockAttackSucceeded() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		String victim = getVictim();
		destToMsgMap.put(DEST_VICTIM, new AttackMsgWrapper(victim, msgGenerator.getMsgVicRockAttSuc()));
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(victim), msgGenerator.getMsgAllRockAttSuc()));
		
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> stealAttackSucceeded() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		String victim = getVictim();
		String attacker = getAttacker();
		destToMsgMap.put(DEST_VICTIM, new AttackMsgWrapper(victim, msgGenerator.getMsgVicStealAttSuc()));
		destToMsgMap.put(DEST_ATTACKER, new AttackMsgWrapper(attacker, msgGenerator.getMsgAttStealAttSuc()));
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(victim, attacker), msgGenerator.getMsgAllStealAttSuc()));

		nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> stealAttackFailed() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(), msgGenerator.getMsgAllStealAttFailed()));
				
		nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> riverAttackSucceeded() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);

		String attacker = getAttacker();
		destToMsgMap.put(DEST_ATTACKER, new AttackMsgWrapper(attacker, msgGenerator.getMsgAllRiverAttSuc()));
		
		nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> treeAttackSucceeded() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		String attacker = getAttacker();
		destToMsgMap.put(DEST_ATTACKER, new AttackMsgWrapper(attacker, msgGenerator.getMsgTreeAttSuc(getSpecialCardsOwners())));
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(attacker), msgGenerator.getMsgAllTreeAtt()));

		nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> treeAttackFailed() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);

		String attacker = getAttacker();
		destToMsgMap.put(DEST_ATTACKER, new AttackMsgWrapper(attacker, msgGenerator.getMsgTreeAttFail()));
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(attacker), msgGenerator.getMsgAllTreeAtt()));
		
		nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> natureDisasterAttackSucceeded() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);
		
		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(), msgGenerator.getMsgAllNatureAttSuc()));

		nextAttackState();
		return destToMsgMap;
	}
	
	public Map<Integer, AttackMsgWrapper> natureDisasterAttackFailed() {
		Map<Integer, AttackMsgWrapper> destToMsgMap = new HashMap<>(DEST_NUM);

		destToMsgMap.put(DEST_ALL, new AttackMsgWrapper(playersManager.getActivePlayersIds(), msgGenerator.getMsgAllNatureAttFail()));

		nextAttackState();
		return destToMsgMap;
	}
	
	
	

	
	
	
	
	
	
	
	
	public AbstractCard getPlayerRandomCard(Player victim) {
    	Random rand = new Random();
    	int max = victim.getNumOfCards()-1;
    	int min = 0;
		int randomCardIndex = min + rand.nextInt(max-min);
		return victim.getCardInIndex(randomCardIndex);
    }
    
    
    public AbstractCard stealPlayerCard(Player victim, Player thief) {
    	AbstractCard c = getPlayerRandomCard(victim);
    	((AbstractPlayableCard)c).setCardUsed(victim);
    	thief.addCardToPlayerHand(c);
    	return c;
    }
    
    public Boolean isSpecialCard(AbstractCard c, String newOwner) {
    	if (cardsManager.isCardSpecial(c.getId())) {
    		((AbstractOwnerableCard) c).setOwners(newOwner);
    		return true;
    	}
    	return false;
    }

    public AbstractCard removePlayerCard(Player victim) {
    	AbstractCard c = getPlayerRandomCard(victim);
    	((AbstractPlayableCard)c).setCardUsed(victim);
    	return c;
    }
    
    public List<String> getSpecialCardsOwners() {
    	Set<String> ownersIds = cardsManager.getSpecialCardsOwners();
    	Function<String, String> mapIdToName = ownerId -> playersManager.getPlayerName(ownerId);
		return ownersIds.stream()
    			.map(mapIdToName)
    			.collect(Collectors.toList());
    }
    
    public void blockNextPlayer() {
    	turns.setBlocked(true);
    }
    
	public void playerDefending(AbstractCard defenseCard, Player victim, Boolean isFlyingSheep) {
		setHelperCard(defenseCard);
		if (isVictimDefended()) {
			if (isFlyingSheep) {
				victim.removeAllExceptFlyingSheepCards(defenseCard.getId());
			}
			finishAttackFail();
		}
		else {
			victimLoseAttack();
		}
	}
	
	public void victimLoseAttack() {
		finishAttackSuc();
	}
    
}
