package card;

import java.util.List;

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
import database.entity.StringEntity;
import globals.Configs;
import globals.Constants;



public class CardFactory {
	private static final Logger logger = LogManager.getLogger(CardFactory.class);
	private StealCard stealCard;
	private Configs configs;
	
	private static CardFactory factory_instance = null;
	
	private CardFactory() {
		configs = Configs.getInstance();
    }
    
	public static CardFactory getInstance() {
    	if (factory_instance == null) {
    		factory_instance = new CardFactory();
    	}
    	return factory_instance;
    }
	
	public AbstractCard createCard(String type, 
									int typeId, 
									int id, 
									String name,
									int txtColor,
									String img, 
									String frame,
									String back,
									int val, 
									String points,
									List<StringEntity> strings) {
		AbstractCard card = null;
		
		logger.info("creating " + type);
		
		if (type.equals(configs.getStringProperty(Constants.TYPE_PIT))) {
			card = new PitCard(typeId, id, name, img, strings);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_CLIFF))) {
			card = new CliffCard(typeId, id, name, img, strings);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_AVALANCHE))) {
			card = new AvalancheCard(typeId, id, name, img, strings);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_RIVER))) {
			card = new RiverCard(typeId, id, name, txtColor, img, frame, back, val, points, strings);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_ROCK))) {
			card = new RockCard(typeId, id, name, txtColor, img, frame, back, val, points, strings);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_TREE))) {
			card = new TreeCard(typeId, id, name, txtColor, img, frame, back, val, points, strings);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_FLYING))) {
			card = new FlyingSheepCard(typeId, id, name, txtColor, img, frame, back, val, points);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_DOG))) {
			card = new DogCard(typeId, id, name, txtColor, img, frame, back);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_STICK))) {
			card = new StickCard(typeId, id, name, txtColor, img, frame, back);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_HUSBAND))) {
			card = new HusbandCard(typeId, id, name, txtColor, img, frame, back);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_WIFE))) {
			card = new WifeCard(typeId, id, name, txtColor, img, frame, back);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_BOMB))) {
			card = new BombCard(typeId, id, name, txtColor, img, frame, back);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_SUPER))) {
			card = new SuperFlyingCard(typeId, id, name, txtColor, img, frame, back);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_REGULAR))) {
			card = new RegularCard(typeId, id, name, txtColor, img, frame, back, val, points);
		}
		else if (type.equals(configs.getStringProperty(Constants.TYPE_STEAL))) {
			card = new StealCard(typeId, id, name, txtColor, img, frame, back, val, points, strings);
			stealCard = (StealCard) card;
		}
		else {
			logger.error("No card created...");
		}
			
		logger.info("created" + card.getName());
		return card;
	}
	

    public StealCard getStealCard() {
		return stealCard;
	}


}
