package game.attacks;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import card.types.AbstractPlayableCard;
import game.Game;
import game.cards.CardsManager;
import game.players.Player;
import game.players.PlayersManager;
import game.turns.TurnsLinkedList;

public class AttacksGenerator {
	private static final Logger logger = LogManager.getLogger(AttacksGenerator.class);
	private AttackHandler attackHandler;
	
	public AttacksGenerator(AttackHandler attackHandler) {
		this.attackHandler = attackHandler;
	}
	
	public void doRockAttack() {
    	Player victim = attackHandler.getPlayerVictim();
    	AbstractCard removedCard = attackHandler.removePlayerCard(victim);
    	attackHandler.setHelperCard(removedCard);
    	attackHandler.nextAttackState();
    }
    
    public void doRiverAttack() {
    	attackHandler.blockNextPlayer();
    	attackHandler.nextAttackState();
    }
    
    public void doTreeAttack() {
    	List<String> owners = attackHandler.getSpecialCardsOwners();
    	attackHandler.nextAttackState();
    	if (owners.isEmpty()) {
    		attackHandler.finishAttackFail();
    	}
    	else {
    		attackHandler.finishAttackSuc();
    	}
    }
    
    public Boolean doStealAttack() {
    	Player victim = attackHandler.getPlayerVictim();
    	Player thief = attackHandler.getPlayerAttacker();
    	AbstractCard stolenCard = attackHandler.stealPlayerCard(victim, thief);
    	attackHandler.setHelperCard(stolenCard);
    	return attackHandler.isSpecialCard(stolenCard, thief.getId());
    }
    
    public Player doNatureDisasterAttack() {
    	Player victim = attackHandler.getPlayerVictim();
    	return victim;
    }
    
    
    public void startAttackOnOtherPlayer(Player victim) {
    	attackHandler.setVictim(victim);
    	attackHandler.executeState();
    }
    
    public void startAttack(AbstractCard card, Player player) {
		logger.debug("Starting attack " + card.getName() + ", attacker is " + player.getName());
		attackHandler.setAttackCard(card);
		attackHandler.setAttacker(player);
		attackHandler.executeState();
	}
	
	public void startNatureDisasterAttack(AbstractCard card, Player player) {
		attackHandler.setAttackCard(card);
		attackHandler.setVictim(player);
		attackHandler.executeState();
	}
    
}
