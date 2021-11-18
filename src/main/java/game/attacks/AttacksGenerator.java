package game.attacks;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import game.players.Player;

/**
* This class represents the attack generator, that is responsible for handling the attack requests.
* This class starts the actual attack operation.
* 
* The class is using the services of the attack handler class to handle the attacks.
* 
* The class is created by the Game class, and is used by the game manager class.
* 
* @author      Keren Solomon
*/
public class AttacksGenerator {
	private static final Logger logger = LogManager.getLogger(AttacksGenerator.class);
	private AttackHandler attackHandler;
	
	/**
	 * Creates an attack generator to handle the attack requests.
	 * The AttacksGenerator class is created with an attack handler member.
	 * 
	 * It is created by the Game class.
	 * 
	 * @param attackHandler    	represents the attack handler member.
	 */
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
