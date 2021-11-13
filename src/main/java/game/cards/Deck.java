package game.cards;

import static globals.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import card.CardFactory;
import database.DriverSQL;
import database.entity.CardEntity;
import database.entity.DecoreEntity;
import game.GameManager;
import globals.Configs;
import globals.Constants;



public class Deck {
	private static final Logger logger = LogManager.getLogger(Deck.class);
	private Configs conf;
	private Map<Integer,AbstractCard> allCards;		// hashmap with all cards (including the duplicates)
	private List<Integer> deck;				// cards in deck
	private List<Integer> disasterCards;		// nature disaster type cards IDs
	private volatile int totalNumOfCards;			// total number of cards
	private volatile int numOfCards;				// number of cards in deck
	private volatile int numPlayers;				// number of players in game
	
	private CardFactory cardsFactory;
	
	public Deck(int numOfPlayers) {
		conf = Configs.getInstance();
		allCards = new HashMap<>();
		deck = new ArrayList<>();
		disasterCards = new ArrayList<>();
		numPlayers = numOfPlayers;
		numOfCards = NO_CARDS_IN_DECK;
		totalNumOfCards = NO_CARDS_IN_DECK;
		cardsFactory = CardFactory.getInstance();
	}
	
	public AbstractCard getCard(int id) {
		return allCards.get(id);
	}
	
	public AbstractCard getCardFromDeck() {	
		if (numOfCards == NO_CARDS_IN_DECK) {
			return null;
		}
		
		int lastCardId = deck.get(numOfCards-1);
		deck.remove(numOfCards-1);
		numOfCards--;
		
		logger.info("getCardFromDeck: number of cards in deck is: " + numOfCards);
		
		return getCard(lastCardId);
	}
	
	public void shuffle() {
		Random rand = new Random();
		for (int i = 0; i<numOfCards; i++) {
			int pos = i + rand.nextInt(numOfCards-i);
			int tmp = deck.get(pos);
			deck.set(pos, deck.get(i));
			deck.set(i, tmp);
		}
	}
	
	private void addCardToDeck(AbstractCard card) {
		logger.info("Add to deck " + card.getName() + ", index = " + card.getId());
		deck.add(card.getId());
		numOfCards++;
	}
	
	private Boolean isStealCard(CardEntity cardInfo) {
		if (cardInfo.getType().equals(conf.getStringProperty(Constants.TYPE_STEAL))) {
			return true;
		}
		return false;
	}
	
	public void initCards() {
		int numToCreate;
		DecoreEntity cardDecore;
		List<CardEntity> cards = GameManager.getInstance().getCardsInfo();
		
		if (cards != null) {
			for (CardEntity cardEntity : cards) {
				cardDecore = cardEntity.getDecore();
				numToCreate = (cardEntity.getMult() * numPlayers) + (cardEntity.getAdd());
				for (int i=0; i<numToCreate; i++) {
					AbstractCard card = cardsFactory.createCard(
							cardEntity.getType(), 
							cardEntity.getId(), 
							totalNumOfCards, 
							cardEntity.getName(), 
							cardDecore.getTxtCol(), 
							cardDecore.getImg(), 
							cardDecore.getFrameImg(), 
							cardDecore.getBackImg(), 
							cardEntity.getValue(), 
							cardDecore.getPointsImg(), 
							cardEntity.getCardStrings());
					card.registerCallback(GameManager.getInstance());
					allCards.put(totalNumOfCards, card);
					totalNumOfCards++;
					
					if (!card.isPlayable()) {
						disasterCards.add(card.getId());
					}
					else if(isStealCard(cardEntity)) {
						logger.info("steal attack card in not a real card - don't add to deck");
					}
					else {
						addCardToDeck(card);
					}
				}
			}
		}
		
		shuffle();
		logger.info("initCards: total number of cards is " + totalNumOfCards);
		logger.info("initCards: number of cards in deck is " + numOfCards);
	}
	
	public void addDisasterCards() {
		for (int cardId : disasterCards) {
			AbstractCard card = allCards.get(cardId);
			addCardToDeck(card);
		}
		logger.info("done adding cards: total number of cards is " + totalNumOfCards);
		logger.info("done adding cards: number of cards in deck is " + numOfCards);
	}
	
	public void printCardsInDeck() {
		logger.info("\nCards in Deck:");
		for (int id : deck) {
			AbstractCard c = allCards.get(id);
			logger.info("[" + id + "] " + c.getName());
		}
	}
}