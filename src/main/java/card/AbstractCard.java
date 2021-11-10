package card;

import java.util.HashMap;
import java.util.Map;

import clientservershared.CardModel;
import eventnotifications.ICardNotifications;
import game.gameplayers.Player;
import globals.Configs;

public abstract class AbstractCard {
	private int typeId;		// id in database - card type identifier
	private int gameId;		// card id in game - given when created
	private String name;	// name of the card
	private String img;		// name of the image as it written in the android app
	
	protected Configs configs = Configs.getInstance();
	private HashMap<Integer, String> relatedString;
	protected ICardNotifications cardNotifications;
	
	public AbstractCard(int typeId, int gameId, String name, String img) {
		this.typeId = typeId;
		this.gameId = gameId;
		this.name = name;
		this.img = img;
	}
	
	public abstract Boolean playCardFromDeck(Player player);
	public abstract Boolean isPlayable();
	
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

	public void setRelatedString(HashMap<Integer, String> relatedString) {
		this.relatedString = relatedString;
	}

	public void registerCallback (ICardNotifications cardNotifications) {
		this.cardNotifications = cardNotifications;
	}
	
	public abstract CardModel getCardInfo();
    
}
