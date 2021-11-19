package game.cards;

import static card.types.AbstractOwnerableCard.cardFeatureType.*;
import static game.cards.CardsManager.getCardRes.*;
import static globals.Constants.CARD_END_TURN;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import clientservershared.CardModel;
import game.players.Player;

/**
* This class is responsible for managing the cards in the game.
* It holds the Deck class as a member, which represents the deck of cards in the game.
* This class also maintain a list of special cards, which updates during the game.
* 
* This class is created by the Game class, and accessed by the Game and the AttackHandler classes.
* The class suggest several APIs cards related.
* 
* @author      Keren Solomon
*/
public class CardsManager {
	private static final Logger logger = LogManager.getLogger(CardsManager.class);
	private List<AbstractOwnerableCard> specialCards;
	private Deck deck;
	
	/**
	 * Creates a CardsManager class to handle the cards in the game.
	 * The CardsManager is created with a specific deck of cards.
	 * The special cards list is initiated.
	 * 
	 * @param deck    represents the deck of cards in the game.
	 */
	public CardsManager(Deck deck) {
		specialCards = new ArrayList<>();
		this.deck = deck;
	}

	/**
	 * This method is called when a players got a special card from the deck,
	 * the special card now have owners, thus add the card to the special cards list.
	 * 
	 * @param specialCardId    the id of the special card that needs to be added to the list.
	 */
	public void setSpecialCard(int specialCardId) {
		AbstractCard specialCard = deck.getCard(specialCardId);
		specialCards.add((AbstractOwnerableCard) specialCard);
	}
	
	/**
	 * This method checks if cooperation between players is enabled,
	 * meaning, it checks if both cooperatable cards (wife sheep and husband sheep) has owners.
	 * 
	 * @return  true|false	true if both cards are part of the game (not in the deck pile).
	 */
	public Boolean isCooperationEnabled() {
		return specialCards.stream()
				.filter(isCooperatableCard)
				.map(mapCardOwner)
				.collect(Collectors.toSet())
				.size() > 1;
	}
	
	/**
	 * This method checks if a card is a special card.
	 * It is called when a card owner is changed,
	 * (in case a steal attack succeeded and a special card was stolen),
	 * compares the given card id to the cards in the special cards list.
	 * 
	 * @param id    the id of the checked card.
	 * 
	 * @return  true|false	true if the card is special, otherwise false.
	 */
    public Boolean isCardSpecial(int id) {
    	return specialCards.stream()
    			.map(mapCardId)
    			.anyMatch(cardId -> cardId == id);
    }
    
    /**
	 * This method generated a set containing the ids of the special cards owners,
	 * (note that it only takes the winnable special cards - nuclear bomb and super flying sheep).
	 * The method is called as a result of a tree attack.
	 * 
	 * @return  Set<String>	the set containing the owners identifiers (can also be empty).
	 */
    public Set<String> getSpecialCardsOwners() {
    	return specialCards.stream()
				.filter(isWinnableCard)
				.map(mapCardOwner)
				.collect(Collectors.toSet());
    }
	
    /**
	 * This method converts a list of cards, representing the player hand, to a list
	 * of CardModels (the shared card object between the client and the server).
	 * 
	 * @param cardsHand         the list of cards to be converted.
	 * 
	 * @return List<CardModel>	the list of CardModels.
	 */
    public List<CardModel> getCardsModels(List<AbstractCard> cardsHand) {
    	return cardsHand.stream().map(card -> card.getCardInfo()).collect(Collectors.toList());
    }
    
    /**
	 * This method converts a card to a CardModel object,
	 * as CardModel is the shared card object between the client and the server.
	 * This is called each time that the server sends the card information to a client
	 * (new card added to player hand / card used and represented in the used pile).
	 * 
	 * @param cardId        the card id.
	 * 
	 * @return CardModel	the CardModel object containing the card information.
	 */
    public CardModel getCardInfo(int cardId) {
    	AbstractCard c = deck.getCard(cardId);
    	return c.getCardInfo();
    }
    
    /**
	 * This method adds the nature disaster cards to the deck and shuffle it.
	 * The method is called when the game starts, after dealing cards to each player.
	 * 
	 */
    public void addNonPlayableCardsToDeck() {
    	deck.addDisasterCards();
    	deck.shuffle();
    }
    
    /**
	 * This Predicate returns true if the card is a special winnable card,
	 * (nuclear bomb or super flying sheep).
	 * 
	 */
    static Predicate<AbstractOwnerableCard> isWinnableCard = card -> FEATURE_TYPE_WIN.equals(card.getSpecialFeatureType());
    
    /**
	 * This Predicate returns true if the card is a special cooperatable card,
	 * (husband flying sheep or wife flying sheep).
	 * 
	 */
	static Predicate<AbstractOwnerableCard> isCooperatableCard = card -> FEATURE_TYPE_COOP.equals(card.getSpecialFeatureType());
	
	/**
	 * This is a mapping function - gets a card and return the card's owners.
	 * 
	 */
	static Function<AbstractOwnerableCard,String> mapCardOwner = card -> card.getOwner();
	
	/**
	 * This is a mapping function - gets a card and return the card's id.
	 * 
	 */
	static Function<AbstractOwnerableCard,Integer> mapCardId = card -> card.getId();
    
	/**
	 * This method gets the last card from game deck of cards.
	 * It is called in each player turn in the game.
	 * 
	 * in case that the pulled card is null, there are no more cards in game, inform the
	 * Game class with the status to end the game (CARD_NULL result returned)..
	 * 
	 * in case that the pulled card does not require any addition action, return status
	 * to inform the Game class to end the player turn (END_TURN result returned).
	 * 
	 * otherwise, this is an attack and the turn is not ended yet (DONE result returned).
	 * 
	 * @param player        the current playing player that gets the card from deck.
	 * 
	 * @return CARD_NULL | END_TURN | DONE	the enum that indicates about the game status after
	 * 										pulling the card from the deck.
	 */
    public getCardRes getCardFromDeck(Player player) {
    	AbstractCard card = deck.getCardFromDeck();
    	
    	if (null == card) {
    		return CARD_NULL;
    	}
    	
    	logger.debug(player.getName() + " got card from deck: " + card.getName());
    	if (CARD_END_TURN == card.playCardFromDeck(player)) {
    		logger.debug("calling end turn");
    		return END_TURN;
    	}
    	return DONE;
    }
    
    /**
	 * This enum to define the possible result of pulling a card from deck.
	 * After pulling a card, the game can be over if there are no more cards in the deck,
	 * or the player turn can end, or if this is nature disaster card - start an attack.
	 * 
	 */
    public enum getCardRes{
    	CARD_NULL,
    	END_TURN,
    	DONE
    }
}
