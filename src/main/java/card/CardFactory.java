package card;

import static globals.Constants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import card.implementation.attack.RiverCard;
import card.implementation.attack.RockCard;
import card.implementation.attack.TreeCard;
import card.implementation.defense.DogCard;
import card.implementation.defense.FlyingSheepCard;
import card.implementation.defense.StickCard;
import card.implementation.ndisaster.AvalancheCard;
import card.implementation.ndisaster.CliffCard;
import card.implementation.ndisaster.PitCard;
import card.implementation.regular.RegularCard;
import card.implementation.regular.StealCard;
import card.implementation.special.BombCard;
import card.implementation.special.HusbandCard;
import card.implementation.special.SuperFlyingCard;
import card.implementation.special.WifeCard;
import database.entity.CardEntity;

public class CardFactory {
	private static final Logger logger = LogManager.getLogger(CardFactory.class);
	private StealCard stealCard;

	private static CardFactory factory_instance = null;

	public static CardFactory getInstance() {
		if (factory_instance == null) {
			factory_instance = new CardFactory();
		}
		return factory_instance;
	}
	  
	public AbstractCard createCard(CardEntity cEntity, int id) {
		String cardType = cEntity.getType();
		logger.info("creating " + cardType);
		
		BiFunction<CardEntity, Integer, AbstractCard> supplier = cardsSupplier.get(cardType);
		AbstractCard card = supplier.apply(cEntity, id);

		if (TYPE_STEAL.equals(cardType)) {
			stealCard = (StealCard) card;
		}
		
		logger.info("created" + card.getName());
		return card;
	}

	public StealCard getStealCard() {
		return stealCard;
	}
	
	static BiFunction<CardEntity, Integer, AbstractCard> riverCardSupplier = RiverCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> rockCardSupplier = RockCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> treeCardSupplier = TreeCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> dogCardSupplier = DogCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> flyingCardSupplier = FlyingSheepCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> stickCardSupplier = StickCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> avalancheCardSupplier = AvalancheCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> cliffCardSupplier = CliffCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> pitCardSupplier = PitCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> regularCardSupplier = RegularCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> BombCardSupplier = BombCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> superCardSupplier = SuperFlyingCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> husbandCardSupplier = HusbandCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> wifeCardSupplier = WifeCard::new;
	static BiFunction<CardEntity, Integer, AbstractCard> stealCardSupplier = StealCard::new;
	
	final static Map<String, BiFunction<CardEntity, Integer, AbstractCard>> cardsSupplier = new HashMap<>();
	  static {
		  cardsSupplier.put(TYPE_RIVER, riverCardSupplier);
		  cardsSupplier.put(TYPE_ROCK, rockCardSupplier);
		  cardsSupplier.put(TYPE_TREE, treeCardSupplier);
		  cardsSupplier.put(TYPE_DOG, dogCardSupplier);
		  cardsSupplier.put(TYPE_FLYING, flyingCardSupplier);
		  cardsSupplier.put(TYPE_STICK, stickCardSupplier);
		  cardsSupplier.put(TYPE_AVALANCHE, avalancheCardSupplier);
		  cardsSupplier.put(TYPE_CLIFF, cliffCardSupplier);
		  cardsSupplier.put(TYPE_PIT, pitCardSupplier);
		  cardsSupplier.put(TYPE_REGULAR, regularCardSupplier);
		  cardsSupplier.put(TYPE_BOMB, BombCardSupplier);
		  cardsSupplier.put(TYPE_SUPER, superCardSupplier);
		  cardsSupplier.put(TYPE_HUSBAND, husbandCardSupplier);
		  cardsSupplier.put(TYPE_WIFE, wifeCardSupplier);
		  cardsSupplier.put(TYPE_STEAL, stealCardSupplier);
	  }   
}
