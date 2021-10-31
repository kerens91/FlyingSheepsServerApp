package card.types;

import card.AbstractCard;
import clientservershared.CardModel;
import game.player.Player;
import globals.Constants;

public abstract class AbstractPlayableCard extends AbstractCard {
	private int textColor;
	private String back;
	private String frame;
	
	
	public AbstractPlayableCard(int typeId, int gameId, String name, int textColor, String img, String frame, String back) {
		super(typeId, gameId, name, img);
		this.textColor = textColor;
		this.back = back;
		this.frame = frame;
	}

	public Boolean playCardFromDeck(Player player) {
		player.addCardToPlayerHand(this);
		return Constants.CARD_END_TURN;
	}
	
	public abstract void playerPickedCard(Player player);
	
	public void playerPickedCouple(Player player, AbstractPlayableCard c) {
		this.setCardUsed(player);
		c.setCardUsed(player);
	}
	
	public void setCardUsed(Player player) {
		player.removeCardFromPlayerHand(this);
	}
	
	public int getTextColor() {
		return textColor;
	}

	public String getBack() {
		return back;
	}

	public String getFrame() {
		return frame;
	}
	
	@Override
	public CardModel getCardInfo() {
		return new CardModel(getId(), getName(), getImg(), textColor, frame, back, null);
	}
}
