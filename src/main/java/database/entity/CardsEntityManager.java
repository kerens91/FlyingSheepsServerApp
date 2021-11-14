package database.entity;

import static globals.Constants.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

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
			
			card = new CardEntity(TYPE_PIT, "Pit Attack", 0, 2, 0);
			strings.add(new StringEntity(MSG_TYPE_SCREEN, CS_STATE_DO, DEST_VICTIM, MSG_ADD_NULL, "Nature Disaster is coming at you...\nThere is a pit in your road!", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_DO, DEST_ALL, MSG_ADD_VIC, " is going to fall into a pit!", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_SUC, DEST_ALL, MSG_ADD_VIC, " is out of the game...", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_IMAGES, CS_STATE_FAIL, DEST_ALL, MSG_ADD_VIC, " defended himself! he used his ", MSG_ADD_CARD, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "pit", TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity(TYPE_CLIFF, "Cliff Attack", 0, 2, 0);
			strings.add(new StringEntity(MSG_TYPE_SCREEN, CS_STATE_DO, DEST_VICTIM, MSG_ADD_NULL, "Nature Disaster is coming at you...\nThere is a cliff in your road!", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_DO, DEST_ALL, MSG_ADD_VIC, " is going to fall off a cliff!", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_SUC, DEST_ALL, MSG_ADD_VIC, " is out of the game...", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_IMAGES, CS_STATE_FAIL, DEST_ALL, MSG_ADD_VIC, " defended himself! he used his ", MSG_ADD_CARD, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "cliff", TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity(TYPE_AVALANCHE, "Avalanche Attack", 0, 2, 0);
			strings.add(new StringEntity(MSG_TYPE_SCREEN, CS_STATE_DO, DEST_VICTIM, MSG_ADD_NULL, "Nature Disaster is coming at you...\nThere is an avalanche in your road!", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_DO, DEST_ALL, MSG_ADD_VIC, " is going to encounter an avalanche!", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_SUC, DEST_ALL, MSG_ADD_VIC, " is out of the game...", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_IMAGES, CS_STATE_FAIL, DEST_ALL, MSG_ADD_VIC, " defended himself! he used his ", MSG_ADD_CARD, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "avalanche", TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity(TYPE_RIVER, "River Attack", 1, 3, 0);
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_SUC, DEST_ALL, MSG_ADD_ATT, " blocked the way with a river...", MSG_ADD_NULL, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "river", TXT_COLOR_WHITE, "points1", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity(TYPE_ROCK, "Rock Attack", 1, 3, 2);
			strings.add(new StringEntity(MSG_TYPE_LIST, CS_STATE_PRE, DEST_ATTACKER, MSG_ADD_NULL, "Who would you like to throw a rock at?", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_IMAGES, CS_STATE_SUC, DEST_VICTIM, MSG_ADD_NULL, "you lost your ", MSG_ADD_CARD, card));
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_SUC, DEST_ALL, MSG_ADD_ATT, " threw a rock on ", MSG_ADD_VIC, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "rock", TXT_COLOR_WHITE, "points1", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity(TYPE_TREE, "Tree Attack", 1, 3, 0);
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_SUC, DEST_ALL, MSG_ADD_ATT, " is getting secrets from the trees...", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_LIST, CS_STATE_SUC, DEST_ATTACKER, MSG_ADD_NULL, "Rumor has it that the card holders are:", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_FAIL, DEST_ATTACKER, MSG_ADD_NULL, "Rumor has it that there are no owners yet...", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_FAIL, DEST_ALL, MSG_ADD_ATT, " is getting secrets from the trees...", MSG_ADD_NULL, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "tree", TXT_COLOR_WHITE, "points1", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_FLYING, "Flying Sheep", 1, 2, 0);
			decore = new DecoreEntity("framedef", "backdef", "flying", TXT_COLOR_WHITE, "points1", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_DOG, "Shepherd Dog", 0, 3, 0);
			decore = new DecoreEntity("framedef", "backdef", "dog", TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_STICK, "Shepherd Stick", 0, 2, 0);
			decore = new DecoreEntity("framedef", "backdef", "stick", TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_HUSBAND, "Husband Sheep", 0, 0, 1);
			decore = new DecoreEntity("framehusband", "backspecial", "husband", TXT_COLOR_BLACK, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_WIFE, "Wife Sheep", 0, 0, 1);
			decore = new DecoreEntity("framewife", "backspecial", "wife", TXT_COLOR_BLACK, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_BOMB, "Nuclear Bomb", 0, 0, 1);
			decore = new DecoreEntity("framespecial", "backspecial", "bomb", TXT_COLOR_DARK, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_SUPER, "Super Sheep", 0, 0, 1);
			decore = new DecoreEntity("framespecial", "backspecial", "super1", TXT_COLOR_DARK, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Drunk Sheep", 2, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "drunk", TXT_COLOR_WHITE, "points2", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Baby Sheep", 11, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "baby", TXT_COLOR_WHITE, "points11", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Fat Sheep", 8, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "fat", TXT_COLOR_WHITE, "points8", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "RockStar Sheep", 5, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "rockstar", TXT_COLOR_WHITE, "points5", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Athlete Sheep", 18, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "athlete", TXT_COLOR_WHITE, "points18", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Grumpy Sheep", 2, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "grumpy", TXT_COLOR_WHITE, "points2", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Chef Sheep", 7, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "chef", TXT_COLOR_WHITE, "points7", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Golden Sheep", 30, 1, 2);
			decore = new DecoreEntity("framespecial", "backgold", "golden", TXT_COLOR_WHITE, "points30", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Sexy Sheep", 6, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "sexy", TXT_COLOR_WHITE, "points6", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Nerdy Sheep", 10, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "nerd", TXT_COLOR_WHITE, "points10", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Winter Sheep", 12, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "winter", TXT_COLOR_WHITE, "points12", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Summer Sheep", 13, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "summer", TXT_COLOR_WHITE, "points13", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Tired Sheep", 3, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "tired", TXT_COLOR_WHITE, "points3", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Doctor Sheep", 17, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "doctor", TXT_COLOR_WHITE, "points17", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			card = new CardEntity(TYPE_REGULAR, "Fireman Sheep", 8, 1, 2);
			decore = new DecoreEntity("framereg", "backreg", "fireman", TXT_COLOR_WHITE, "points8", card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			strings.clear();
			card = new CardEntity(TYPE_STEAL, NAME_STEAL, 0, 0, 1);
			strings.add(new StringEntity(MSG_TYPE_LIST, CS_STATE_PRE, DEST_ATTACKER, MSG_ADD_ATT, "Who would you like to steal from?", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_SCREEN, CS_STATE_DO, DEST_VICTIM, MSG_ADD_ATT, " is coming at you...\nThey want to steal your herd!", MSG_ADD_NULL, card));
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_DO, DEST_ALL, MSG_ADD_ATT, " is attacking ", MSG_ADD_VIC, card));
			strings.add(new StringEntity(MSG_TYPE_IMAGES, CS_STATE_SUC, DEST_VICTIM, MSG_ADD_ATT, " stole your card ", MSG_ADD_CARD, card));
			strings.add(new StringEntity(MSG_TYPE_IMAGES, CS_STATE_SUC, DEST_ATTACKER, MSG_ADD_NULL, "hey thief! you just stole ", MSG_ADD_CARD, card));
			strings.add(new StringEntity(MSG_TYPE_TITLE, CS_STATE_SUC, DEST_ALL, MSG_ADD_ATT, " stole a card from ", MSG_ADD_VIC, card));
			strings.add(new StringEntity(MSG_TYPE_IMAGES, CS_STATE_FAIL, DEST_ALL, MSG_ADD_VIC, " defended himself! he used his ", MSG_ADD_CARD, card));
			card.setCardStrings(strings);
			decore = new DecoreEntity("frameatt", "backatt", "steal", TXT_COLOR_WHITE, null, card);
			card.setDecore(decore);
			entityManager.persist(card);
			
			entityManager.getTransaction().commit();
			
			printCardDb();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
