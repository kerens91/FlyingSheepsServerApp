package game.cards;

import static globals.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

/**
* This class represents the game deck of cards.
* A deck is a pile of cards, from which the players get their new cards.
* Each turn in the game ends with the player pulling a card from the deck.
* 
* First, the deck is initiated with all the playable cards in the game.
* Then, shuffle the deck, and deal five cards to each of the player in game.
* Finally, add the nature disaster cards to the deck, and shuffle again.
* 
* Each time a player pulls a card from the deck, the card is marked as used,
* and removed from the deck.
* 
* The deck itself is represented by a list of cards IDs.
* The class also holds a HashMap containing all cards in the game, the cards are
* the values in the map, and are mapped by their IDs.
* When creating the cards for the game, the Deck class is using the CardsFactory
* class to create the cards.
* 
* The class also maintain a few more values:
* <ul>
* <li>numOfCards - represents the number of cards in the deck.
* <li>totalNumOfCards - represents the number of all the cards in the game.
* <li>numPlayers - represents the number of player in the game.
* </ul>
*  
* This class is created by the Game class, and accessed by the Game, CardsManager
* and the AttackHandler classes.
* The class suggest several APIs deck related.
* 
* @author      Keren Solomon
*/
public class Deck {
	private static final Logger logger = LogManager.getLogger(Deck.class);
	private CardFactory cardsFactory;

	private Map<Integer,AbstractCard> allCards;
	private List<Integer> deck;
	private List<Integer> disasterCards;

	private int totalNumOfCards;
	private int numOfCards;
	private int numPlayers;
	
	/**
	 * Creates a Deck to handle the operations related to cards initiation
	 * and the deck maintenance.
	 * The Deck class is created with the number of players in game.
	 * 
	 * The lists are initiated as empty lists, and number of cards is initiated to 0.
	 * Also getting an instance of the card factory.
	 * 
	 * @param numOfPlayers    represents the number of players in the game.
	 */
	public Deck(int numOfPlayers) {
		cardsFactory = CardFactory.getInstance();

		allCards = new HashMap<>();
		deck = new ArrayList<>();
		disasterCards = new ArrayList<>();

		numPlayers = numOfPlayers;
		numOfCards = NO_CARDS_IN_DECK;
		totalNumOfCards = NO_CARDS_IN_DECK;

	}

	/**
	 * This method is responsible for accessing a card by a given id.
	 * It is used by the Game class and by the cards manager.
	 * 
	 * @param id    the id of the requested card.
	 * 
	 * @return  AbstractCard	the requested card.
	 */
	public AbstractCard getCard(int id) {
		return allCards.get(id);
	}

	/**
	 * This method is responsible for pulling a card from the deck.
	 * It is called by the cards manager each time a player needs a new card.
	 * 
	 * @return  AbstractCard	the pulled card.
	 */
	public AbstractCard getCardFromDeck() {	
		if (isDeckEmpty.get()) {
			return null;
		}
		return allCards.get(removeCardFromDeck());
	}
	
	/**
	 * This method is responsible for shuffling the deck of cards.
	 * It is used after creating the cards and inserting them to the deck,
	 * that way, when pulling the last card from the deck it will be a random card.
	 *  
	 */
	public void shuffle() {
		for (int i = 0; i<numOfCards; i++) {
			int pos = i + randomValue.apply(i);
			int tmp = deck.get(pos);
			deck.set(pos, deck.get(i));
			deck.set(i, tmp);
		}
	}

	/**
	 * This method is inserting a new card to the game deck.
	 * This is called as part of the cards deck initiation.
	 * 
	 * @param card    the card to be added to the deck.
	 * 
	 */
	private void addCardToDeck(AbstractCard card) {
		logger.info("Add to deck " + card.getName() + ", index = " + card.getId());
		deck.add(card.getId());
		numOfCards++;
	}

	/**
	 * This method is responsible for removing a card from the deck.
	 * It is called after a card is pulled from the deck.
	 * 
	 * @return  lastCardId	the int represents the the removed card id.
	 */
	private int removeCardFromDeck() {
		int lastCardId = deck.get(numOfCards-1);
		deck.remove(numOfCards-1);
		numOfCards--;
		return lastCardId;
	}

	/**
	 * This method is a helper method for the cards initiation.
	 * It is responsible for handling each card entity, process it's data and convert it
	 * to a card (each card has it's type), by calling the cards factory create card method.
	 * 
	 * The playable cards are added to the deck, and the nature disaster cards are kept in a list.
	 * 
	 * @param cards    the list of cards entities.
	 * 
	 */
	private void initCardHelper(List<CardEntity> cards) {
		logger.debug("Initiating cards");
		int numToCreate;
		
		for (CardEntity cardEntity : cards) {
			
			numToCreate = getNumCardsToCreate.apply(cardEntity.getMult(), cardEntity.getAdd());
			while (numToCreate > 0) {
				AbstractCard card = cardsFactory.createCard(cardEntity,	totalNumOfCards);
				card.registerCallback(GameManager.getInstance());
				allCards.put(totalNumOfCards, card);
				totalNumOfCards++;

				if (!isPlayableCard.test(card)) {
					disasterCards.add(card.getId());
				}
				
				if (isStealCard.negate().and(isPlayableCard).test(card)) {
					addCardToDeck(card);
				}
				
				numToCreate--;
			}
		}
	}

	/**
	 * This method is responsible for the cards initiation.
	 * 
	 * It is using the data from the database, represented by a list of entities,
	 * and calling a helper method to handle each entity.
	 * 
	 * when done - shuffle the deck.
	 * 
	 */
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

	/**
	 * This method is responsible for adding the disaster cards to the deck.
	 * It is called after all players are getting their cards and the game is
	 * about to start.
	 * 
	 */
	public void addDisasterCards() {
		numOfCards += disasterCards.size();
		deck = Stream.concat(disasterCards.stream(), deck.stream())
				.collect(Collectors.toList());
	}

	/**
	 * A BiFunction that calculates the number of cards that need to create per card type.
	 * 
	 * @param mult    the multiplier.
	 * @param add     the addition.
	 * 
	 * @return  the int represents the number of cards to create.
	 */
	private BiFunction<Integer, Integer, Integer> getNumCardsToCreate = 
			(mult, add) -> mult * numPlayers + add;
	
	/**
	* A Predicate that indicated is a card is of type steal card.
    * 
	* @param card    the checked card.
	* @return  true if the card is a StealCard, otherwise false.
	*/
	private Predicate<AbstractCard> isStealCard = card -> 
			card.getName().equals(StealCard.stealCardNameSupplier.get());
	
	/**
	* A Function that generates a random value.
	* 
	*/
	private	Function<Integer, Integer> randomValue = (index) ->
			(int)Math.floor(Math.random() * (numOfCards-index));
		
	/**
	* A Predicate that indicates if a card is playable card.
	* 
	*/
	private Predicate<AbstractCard> isPlayableCard = AbstractCard::isPlayable;
	
	/**
	* A Supplier of type boolean that indicates if the deck of cards is empty.
	* 
	*/
	private Supplier<Boolean> isDeckEmpty = () -> NO_CARDS_IN_DECK == numOfCards;
	
}
