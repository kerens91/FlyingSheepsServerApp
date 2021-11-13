package game.cards;

import static card.types.AbstractOwnerableCard.cardFeatureType.*;
import static game.cards.CardsManager.getCardRes.*;
import static globals.Constants.CARD_END_TURN;
import static globals.Constants.COUPLE_PICKED_CARDS;
import static globals.Constants.NO_PICKED_CARDS;
import static globals.Constants.SINGLE_PICKED_CARD;

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
import clientservershared.PickedCards;
import game.players.Player;

public class CardsManager {
	private static final Logger logger = LogManager.getLogger(CardsManager.class);
	private List<AbstractOwnerableCard> specialCards;
	private Deck deck;
	
	static Predicate<AbstractOwnerableCard> isWinnableCard = card -> FEATURE_TYPE_WIN.equals(card.getSpecialFeatureType());
	static Predicate<AbstractOwnerableCard> isCooperatableCard = card -> FEATURE_TYPE_COOP.equals(card.getSpecialFeatureType());
	static Function<AbstractOwnerableCard,String> mapCardOwner = card -> card.getOwner();
	static Function<AbstractOwnerableCard,Integer> mapCardId = card -> card.getId();
	
	public CardsManager(Deck deck) {
		specialCards = new ArrayList<>();
		this.deck = deck;
	}

	public void setSpecialCard(int specialCardId) {
		AbstractCard specialCard = deck.getCard(specialCardId);
		specialCards.add((AbstractOwnerableCard) specialCard);
	}
	
	public Boolean isCooperationEnabled() {
		return specialCards.stream()
				.filter(isCooperatableCard)
				.map(mapCardOwner)
				.collect(Collectors.toSet())
				.size() > 1;
	}
	
	/* this is called when a card owners is changed (steal) so I can assume that the card must be in the array */
    public Boolean isCardSpecial(int id) {
    	return specialCards.stream()
    			.map(mapCardId)
    			.anyMatch(cardId -> cardId == id);
    }
    
    public Set<String> getSpecialCardsOwners() {
    	return specialCards.stream()
				.filter(isWinnableCard)
				.map(mapCardOwner)
				.collect(Collectors.toSet());
    }
	
    public List<CardModel> getCardsModels(List<AbstractCard> cardsHand) {
    	return cardsHand.stream().map(card -> card.getCardInfo()).collect(Collectors.toList());
    }
    
    public CardModel getCardInfo(int cardId) {
    	AbstractCard c = deck.getCard(cardId);
    	return c.getCardInfo();
    }
    
    public void addNonPlayableCardsToDeck() {
    	deck.addDisasterCards();
    	deck.shuffle();
    	deck.printCardsInDeck();
    }
    

    
    public getCardRes getCardFromDeck(Player player) {
    	AbstractCard card = deck.getCardFromDeck();
    	
    	if (null == card) {
    		return CARD_NULL;
    	}
    	
    	logger.debug("get card from deck: " + card.getName());
    	if (CARD_END_TURN == card.playCardFromDeck(player)) {
    		logger.debug("calling end turn");
    		return END_TURN;
    	}
    	return DONE;
    }
    
    public enum getCardRes{
    	CARD_NULL,
    	END_TURN,
    	DONE
    }
}
