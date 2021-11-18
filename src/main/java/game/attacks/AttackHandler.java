package game.attacks;

import static globals.Constants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import attackstate.GameAttackState;
import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import card.types.AbstractPlayableCard;
import game.cards.CardsManager;
import game.players.Player;
import game.players.PlayersManager;
import game.turns.TurnsLinkedList;

/**
* This class represents the attack handler, the class that deals with the attacks in the game.
* The class manages the attack states, and supplies set of APIs to handle the attacks.
* 
* The class is created by the Game class, and is used by the attack resolver and the attack generator.
* 
* The class is using the services of the following members:
* <ul>
* <li>attackState - handles the states of the attack.
* <li>playersManager - handles the attacker and victim players of the attack.
* <li>msgGenerator - generates attack messages that the server sends to the clients during the attacks.
* <li>cardsManager - handles the attack card and the defense card of the attack.
* <li>turns - handles the turns in game.
* </ul>
* each of the members is initiated by the Game class and sent to this class when it is created.
* 
* @author      Keren Solomon
*/
public class AttackHandler {
	private static final Logger logger = LogManager.getLogger(AttackHandler.class);
	private GameAttackState attackState;
	private PlayersManager playersManager;
	private AttackMsgGenerator msgGenerator;
	private CardsManager cardsManager;
	private TurnsLinkedList turns;
	
	/**
	 * Creates an attack handler to handle the attacks in the game.
	 * The AttackHandler class is created with all requires members to this class.
	 * 
	 * It is created by the Game class.
	 * 
	 * @param attackState    	represents the GameAttackState that handles the states of the attack.
	 * @param playersManager    represents the PlayersManager that handles the attacker and victim players of the attack.
	 * @param msgGenerator    	represents the AttackMsgGenerator that generates attack messages
	 * 							that the server sends to the clients during the attacks.
	 * @param cardsManager    	represents the CardsManager that handles the attack card and the defense card of the attack.
	 * @param turns    			represents the TurnsLinkedList that handles the turns in game.
	 */
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
		attackState.registerNotifications();
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
    	return (attackState.getVictim().getId().equals(player.getId()));
    }
    
    public Boolean isAttackActive() {
    	return attackState.isAttackActive();
    }
    
    public Boolean isVictimDefended() {
    	return attackState.victimDefended();
    }
    
    public PlayersManager getPlayersManager() {
		return playersManager;
	}

	public AttackMsgGenerator getMsgGenerator() {
		return msgGenerator;
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
