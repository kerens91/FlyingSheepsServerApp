package card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import attackmsg.AttackMsgInfo;
import clientservershared.CardModel;
import database.entity.StringEntity;
import eventnotifications.ICardNotifications;
import game.players.Player;
import globals.Configs;

/**
* Abstract Card class defines the basic card object.
* A card will have:
* <ul>
* <li>typeId - represents the id in database - card type identifier
* <li>gameId - represents the card id in game - given when the instance is created
* <li>name - represents the name of the card
* <li>img - name of the image as it written in the android app
* <li>relatedString - represents an array of strings related to the card
* </ul>
* Each card is created with the above values, and it is not changeable.
* The cards are created by the CardFactory class, based on the data
* from the database.
* There will be multiple cards in a game, divided into cards types,
* all inherits this basic abstract card class.
* A card can send events to the GameManager class.
* Each card will implement the abstract method playCardFromDeck, 
* depends on it's type.
* The cards in the game are handled by the CardsManager class.
* 
* @author      Keren Solomon
*/
public abstract class AbstractCard {
	private final int typeId;
	private final int gameId;
	private final String name;
	private final String img;
	private Map<Integer, String> relatedString;
	
	protected Configs configs = Configs.getInstance();
	protected ICardNotifications cardNotifications;
	
	public AbstractCard(int typeId, int gameId, String name, String img) {
		this.typeId = typeId;
		this.gameId = gameId;
		this.name = name;
		this.img = img;
	}
	
	public abstract Boolean playCardFromDeck(Player player);
	public abstract Boolean isPlayable();
	public abstract CardModel getCardInfo();
	
	public int getId() {
		return gameId;
	}
	
	public int getType() {
		return typeId;
	}

	public String getName() {
		return name;
	}

	public String getImg() {
		return img;
	}

	public Map<Integer, String> getRelatedString() {
		return relatedString;
	}

	public void setRelatedString(Map<Integer, String> relatedString) {
		this.relatedString = relatedString;
	}

	public void registerCallback (ICardNotifications cardNotifications) {
		this.cardNotifications = cardNotifications;
	}
    
	/**
	 * This method is called when the card is created.
	 * This method is responsible for initiating the strings map holding the related card
	 * string messages that are displayed to the players screen during the game.
	 * 
	 * this method is used by the attack cards and nature disaster cards.
	 * 
	 * This method get a list of string entities, and map it based on the attack card state,
	 * and the message receiver.
	 * 
	 * @param strings  the card related string messages.
	 * 
	 * @return  the strings mapped by state and destination.
	 */
	public Map<Integer,Map<Integer,AttackMsgInfo>> initCardsStrings(List<StringEntity> strings) {
		Map<Integer, Map<Integer, AttackMsgInfo>> stateToStringMap = new HashMap<>();
		
		strings.stream()
			.map(StringEntity::getState)
			.collect(Collectors.toSet())
			.forEach(state -> stateToStringMap.put(state, new HashMap<>()));
		
		strings.stream()
			.forEach(entity -> 
			stateToStringMap.get(entity.getState())
			.put(entity.getDest(), 
					new AttackMsgInfo(entity.getType(), entity.getPre(), entity.getString(), entity.getPost())));
					
		return stateToStringMap;
	}
}
