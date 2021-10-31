package database.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import globals.Constants;

public class CardsEntityManager {
	@PersistenceContext
    private EntityManager entityManager;
	
	public void connectToDb() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("cards_catalog");
		entityManager = emf.createEntityManager();
	}
	
	public void disConnectFromDb() {
		entityManager.close();
	}
	
	public EntityManager getCardsEntityManager() {
		return entityManager;
	}
	
	
	public void printCardDb() {
		List<CardEntity> cards = getCards();
		for (CardEntity card : cards) {
			card.printCard();
		}
	}
	
	public List<CardEntity> getCards() {
		List<CardEntity> cards = null;
		try {
			cards = entityManager.createQuery("select e from CardEntity e", CardEntity.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cards;
	}
	
	public void initiateCardsValues() {
		CardEntity card;
		DecoreEntity decore;
		List<StringEntity> strings = new ArrayList<>();
		
		try {
			entityManager.getTransaction().begin();
			
			card = new CardEntity("pit", "Pit Attack", 0, 2, 0);
			strings.add(new StringEntity(Constants.MSG_TYPE_SCREEN, Constants.CS_STATE_DO, Constants.DEST_VICTIM, Constants.MSG_ADD_NULL, "Nature Disaster is coming at you...\nThere is a pit in your road!", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_DO, Constants.DEST_ALL, Constants.MSG_ADD_VIC, " is going to fall into a pit!", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_SUC, Constants.DEST_ALL, Constants.MSG_ADD_VIC, " is out of the game...", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_IMAGES, Constants.CS_STATE_FAIL, Constants.DEST_ALL, Constants.MSG_ADD_VIC, " defended himself! he used his ", Constants.MSG_ADD_CARD, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "pit", Constants.TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity("cliff", "Cliff Attack", 0, 2, 0);
			strings.add(new StringEntity(Constants.MSG_TYPE_SCREEN, Constants.CS_STATE_DO, Constants.DEST_VICTIM, Constants.MSG_ADD_NULL, "Nature Disaster is coming at you...\nThere is a cliff in your road!", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_DO, Constants.DEST_ALL, Constants.MSG_ADD_VIC, " is going to fall off a cliff!", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_SUC, Constants.DEST_ALL, Constants.MSG_ADD_VIC, " is out of the game...", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_IMAGES, Constants.CS_STATE_FAIL, Constants.DEST_ALL, Constants.MSG_ADD_VIC, " defended himself! he used his ", Constants.MSG_ADD_CARD, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "cliff", Constants.TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity("avalanche", "Avalanche Attack", 0, 2, 0);
			strings.add(new StringEntity(Constants.MSG_TYPE_SCREEN, Constants.CS_STATE_DO, Constants.DEST_VICTIM, Constants.MSG_ADD_NULL, "Nature Disaster is coming at you...\nThere is an avalanche in your road!", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_DO, Constants.DEST_ALL, Constants.MSG_ADD_VIC, " is going to encounter an avalanche!", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_SUC, Constants.DEST_ALL, Constants.MSG_ADD_VIC, " is out of the game...", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_IMAGES, Constants.CS_STATE_FAIL, Constants.DEST_ALL, Constants.MSG_ADD_VIC, " defended himself! he used his ", Constants.MSG_ADD_CARD, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "avalanche", Constants.TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity("river", "River Attack", 1, 3, 0);
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_SUC, Constants.DEST_ALL, Constants.MSG_ADD_ATT, " blocked the way with a river...", Constants.MSG_ADD_NULL, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "river", Constants.TXT_COLOR_WHITE, "points1", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity("rock", "Rock Attack", 1, 3, 2);
			strings.add(new StringEntity(Constants.MSG_TYPE_LIST, Constants.CS_STATE_PRE, Constants.DEST_ATTACKER, Constants.MSG_ADD_NULL, "Who would you like to throw a rock at?", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_IMAGES, Constants.CS_STATE_SUC, Constants.DEST_VICTIM, Constants.MSG_ADD_NULL, "you lost your ", Constants.MSG_ADD_CARD, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_SUC, Constants.DEST_ALL, Constants.MSG_ADD_ATT, " threw a rock on ", Constants.MSG_ADD_VIC, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "rock", Constants.TXT_COLOR_WHITE, "points1", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity("tree", "Tree Attack", 1, 3, 0);
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_SUC, Constants.DEST_ALL, Constants.MSG_ADD_ATT, " is getting secrets from the trees...", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_LIST, Constants.CS_STATE_SUC, Constants.DEST_ATTACKER, Constants.MSG_ADD_NULL, "Rumor has it that the card holders are:", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_FAIL, Constants.DEST_ATTACKER, Constants.MSG_ADD_NULL, "Rumor has it that there are no owners yet...", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_FAIL, Constants.DEST_ALL, Constants.MSG_ADD_ATT, " is getting secrets from the trees...", Constants.MSG_ADD_NULL, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "tree", Constants.TXT_COLOR_WHITE, "points1", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("flying", "Flying Sheep", 1, 2, 0);
			decore = new DecoreEntity("framedef", "backdef", "flying", Constants.TXT_COLOR_WHITE, "points1", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("dog", "Shepherd Dog", 0, 3, 0);
			decore = new DecoreEntity("framedef", "backdef", "dog", Constants.TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("stick", "Shepherd Stick", 0, 2, 0);
			decore = new DecoreEntity("framedef", "backdef", "stick", Constants.TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("husband", "Husband Sheep", 0, 0, 1);
			decore = new DecoreEntity("framehusband", "backspecial", "husband", Constants.TXT_COLOR_BLACK, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("wife", "Wife Sheep", 0, 0, 1);
			decore = new DecoreEntity("framewife", "backspecial", "wife", Constants.TXT_COLOR_BLACK, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("bomb", "Nuclear Bomb", 0, 0, 1);
			decore = new DecoreEntity("framespecial", "backspecial", "bomb", Constants.TXT_COLOR_DARK, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("super", "Super Sheep", 0, 0, 1);
			decore = new DecoreEntity("framespecial", "backspecial", "super1", Constants.TXT_COLOR_DARK, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Drunk Sheep", 2, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "drunk", Constants.TXT_COLOR_WHITE, "points2", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Baby Sheep", 11, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "baby", Constants.TXT_COLOR_WHITE, "points11", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Fat Sheep", 8, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "fat", Constants.TXT_COLOR_WHITE, "points8", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "RockStar Sheep", 5, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "rockstar", Constants.TXT_COLOR_WHITE, "points5", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Athlete Sheep", 18, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "athlete", Constants.TXT_COLOR_WHITE, "points18", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Grumpy Sheep", 2, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "grumpy", Constants.TXT_COLOR_WHITE, "points2", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Chef Sheep", 7, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "chef", Constants.TXT_COLOR_WHITE, "points7", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Golden Sheep", 30, 1, 2);
			decore = new DecoreEntity("framespecial", "backgold", "golden", Constants.TXT_COLOR_WHITE, "points30", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Sexy Sheep", 6, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "sexy", Constants.TXT_COLOR_WHITE, "points6", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Nerdy Sheep", 10, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "nerd", Constants.TXT_COLOR_WHITE, "points10", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Winter Sheep", 12, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "winter", Constants.TXT_COLOR_WHITE, "points12", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Summer Sheep", 13, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "summer", Constants.TXT_COLOR_WHITE, "points13", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Tired Sheep", 3, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "tired", Constants.TXT_COLOR_WHITE, "points3", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Doctor Sheep", 17, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "doctor", Constants.TXT_COLOR_WHITE, "points17", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity("regular", "Fireman Sheep", 8, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "fireman", Constants.TXT_COLOR_WHITE, "points8", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity("steal", "Steal Attack", 0, 0, 1);
			strings.add(new StringEntity(Constants.MSG_TYPE_LIST, Constants.CS_STATE_PRE, Constants.DEST_ATTACKER, Constants.MSG_ADD_ATT, "Who would you like to steal from?", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_SCREEN, Constants.CS_STATE_DO, Constants.DEST_VICTIM, Constants.MSG_ADD_ATT, " is coming at you...\nThey want to steal your herd!", Constants.MSG_ADD_NULL, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_DO, Constants.DEST_ALL, Constants.MSG_ADD_ATT, " is attacking ", Constants.MSG_ADD_VIC, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_IMAGES, Constants.CS_STATE_SUC, Constants.DEST_VICTIM, Constants.MSG_ADD_ATT, " stole your card ", Constants.MSG_ADD_CARD, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_IMAGES, Constants.CS_STATE_SUC, Constants.DEST_ATTACKER, Constants.MSG_ADD_NULL, "hey thief! you just stole ", Constants.MSG_ADD_CARD, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_TITLE, Constants.CS_STATE_SUC, Constants.DEST_ALL, Constants.MSG_ADD_ATT, " stole a card from ", Constants.MSG_ADD_VIC, card));
			strings.add(new StringEntity(Constants.MSG_TYPE_IMAGES, Constants.CS_STATE_FAIL, Constants.DEST_ALL, Constants.MSG_ADD_VIC, " defended himself! he used his ", Constants.MSG_ADD_CARD, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "steal", Constants.TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			entityManager.getTransaction().commit();
			
			printCardDb();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
