package card.types;

import clientservershared.CardModel;
import game.player.Player;

public abstract class AbstractValueableCard extends AbstractPlayableCard {
	private int numOfPoints;
	private String pointsImg;
	
	public AbstractValueableCard(int typeId, int gameId, String name, int textColor, String img, String frame, String back, int value, String pointsImg) {
		super(typeId, gameId, name, textColor, img, frame, back);
		numOfPoints = value;
		this.pointsImg = pointsImg;
	}
	
	public Boolean playCardFromDeck(Player player) {
		player.addPointsToPlayerScore(numOfPoints);
		return super.playCardFromDeck(player);
	}
	
	public void setCardUsed(Player player) {
		player.dropPointsFromPlayerScore(numOfPoints);
		super.setCardUsed(player);
	}
	
	public int getPoints() {
		return numOfPoints;
	}

	public String getPointsImg() {
		return pointsImg;
	}

	@Override
	public CardModel getCardInfo() {
		CardModel info = super.getCardInfo();
		info.setPoints(pointsImg);
		return info;
	}
	
}
