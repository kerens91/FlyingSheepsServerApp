package game.cards;

import static globals.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import card.CardFactory;
import card.implementation.regular.StealCard;
import database.entity.CardEntity;
import game.GameManager;

public class Deck {
	private static final Logger logger = LogManager.getLogger(Deck.class);
	private CardFactory cardsFactory;

	private Map<Integer,AbstractCard> allCards;		// hashmap with all cards (including the duplicates)
	private List<Integer> deck;				// cards in deck
	private List<Integer> disasterCards;		// nature disaster type cards IDs

	private int totalNumOfCards;			// total number of cards
	private int numOfCards;				// number of cards in deck
	private int numPlayers;				// number of players in game

	public Deck(int numOfPlayers) {
		cardsFactory = CardFactory.getInstance();

		allCards = new HashMap<>();
		deck = new ArrayList<>();
		disasterCards = new ArrayList<>();

		numPlayers = numOfPlayers;
		numOfCards = NO_CARDS_IN_DECK;
		totalNumOfCards = NO_CARDS_IN_DECK;

	}

	public AbstractCard getCard(int id) {
		return allCards.get(id);
	}

	public AbstractCard getCardFromDeck() {	
		if (isDeckEmpty.get()) {
			return null;
		}
		return allCards.get(removeCardFromDeck());
	}
	
	public void shuffle() {
		for (int i = 0; i<numOfCards; i++) {
			int pos = i + randomValue.apply(i);
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

	private int removeCardFromDeck() {
		int lastCardId = deck.get(numOfCards-1);
		deck.remove(numOfCards-1);
		numOfCards--;
		return lastCardId;
	}

	private void initCardHelper(List<CardEntity> cards) {
		int numToCreate;
		
		for (CardEntity cardEntity : cards) {
			
			numToCreate = getNumCardsToCreate.apply(cardEntity.getMult(), cardEntity.getAdd());
			while (numToCreate > 0) {
				AbstractCard card = cardsFactory.createCard(cardEntity,	totalNumOfCards);
				card.registerCallback(GameManager.getInstance());
				allCards.put(totalNumOfCards, card);
				totalNumOfCards++;

				if (isNatureDisasterCard.test(card)) {
					disasterCards.add(card.getId());
				}
				
				if (isPlayableCard.and(isStealCard).negate().test(card)) {
					addCardToDeck(card);
				}
				
				numToCreate--;
			}
		}
	}

	public void initCards() {
		List<CardEntity> cards = GameManager.getInstance().getCardsInfo();

		Optional.ofNullable(cards)
		.ifPresentOrElse(
				cardslist -> {
					initCardHelper(cards);
				},
				() -> logger.error("Could't get cards from database"));

		shuffle();
	}

	public void addDisasterCards() {
		numOfCards += disasterCards.size();
		deck = Stream.concat(disasterCards.stream(), deck.stream())
				.collect(Collectors.toList());
	}

	private BiFunction<Integer, Integer, Integer> getNumCardsToCreate = 
			(mult, add) -> mult * numPlayers + add;
			
	private Predicate<AbstractCard> isStealCard = card -> 
			card.getName().equals(StealCard.stealCardNameSupplier.get());
			
	private	Function<Integer, Integer> randomValue = (index) ->
			(int)Math.floor(Math.random() * (numOfCards-index));
			
	private Predicate<AbstractCard> isPlayableCard = AbstractCard::isPlayable;
	private Predicate<AbstractCard> isNatureDisasterCard = isPlayableCard.negate();
	private Supplier<Boolean> isDeckEmpty = () -> NO_CARDS_IN_DECK == numOfCards;
	
}
