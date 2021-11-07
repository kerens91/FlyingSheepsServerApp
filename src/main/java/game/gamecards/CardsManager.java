package game.gamecards;

import static card.types.AbstractOwnerableCard.cardFeatureType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import card.AbstractCard;
import card.types.AbstractOwnerableCard;
import globals.Configs;

public class CardsManager {
	private List<AbstractOwnerableCard> specialCards;
	
	static Predicate<AbstractOwnerableCard> isWinnableCard = card -> FEATURE_TYPE_WIN.equals(card.getSpecialFeatureType());
	static Predicate<AbstractOwnerableCard> isCooperatableCard = card -> FEATURE_TYPE_COOP.equals(card.getSpecialFeatureType());
	static Function<AbstractOwnerableCard,String> mapCardOwner = card -> card.getOwner();
	static Function<AbstractOwnerableCard,Integer> mapCardId = card -> card.getId();
	
	public CardsManager() {
		specialCards = new ArrayList<>();
	}

	public void setSpecialCard(AbstractCard specialCard) {
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
	
}
