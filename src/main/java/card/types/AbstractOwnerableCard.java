package card.types;

import card.interfaces.ICoupledCard;
import game.gameplayers.Player;

public abstract class AbstractOwnerableCard extends AbstractPlayableCard implements ICoupledCard {
	private String owner;
	
	public AbstractOwnerableCard(int typeId, int gameId, String name, int textColor, String img, String frame, String back) {
		super(typeId, gameId, name, textColor, img, frame, back);
	}
	
	public Boolean playCardFromDeck(Player player) {
		setOwners(player.getId());
		cardNotifications.onGotFromDeckSpecial(getId());
		return super.playCardFromDeck(player);
	}

	@Override
	public void playerPickedCard(Player player) {
		setCardUsed(player);
	}
	
	@Override
	public void playerPickedCouple(Player player, AbstractPlayableCard c) {
		super.playerPickedCouple(player, c);
		if (isCouple(c)) {
			doCouple(player);
		}	
	}
	
	public void setOwners(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}
	
	public abstract cardFeatureType getSpecialFeatureType();
	
	public enum cardFeatureType {
		FEATURE_TYPE_COOP,
		FEATURE_TYPE_WIN
	}
}
