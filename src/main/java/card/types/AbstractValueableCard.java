package card.types;

import clientservershared.CardModel;
import game.players.Player;

/**
* Abstract Valueable Card class defines the cards that have a numeric value.
* The player that holds that card will have more points in the game, according
* to the values of the cards in his hand.
*  
* This class inherits from the AbstractPlayableCard class.
* 
* @author      Keren Solomon
*/
public abstract class AbstractValueableCard extends AbstractPlayableCard {
	private int numOfPoints;
	private String pointsImg;
	
	public AbstractValueableCard(int typeId, int gameId, String name, int textColor, String img, String frame, String back, int value, String pointsImg) {
		super(typeId, gameId, name, textColor, img, frame, back);
		numOfPoints = value;
		this.pointsImg = pointsImg;
	}
	
	/**
	 * This method is called when a player draws this card from the deck.
	 * The card value is added to the player total number of points.
	 * 
	 * This method indicates about the current player turn if ended.
	 * 
	 * @param player  the player that got this card from deck.
	 * 
	 * @return  true|false	true if the turn ended, otherwise false.
	 */
	public Boolean playCardFromDeck(Player player) {
		player.addPointsToPlayerScore(numOfPoints);
		return super.playCardFromDeck(player);
	}
	
	/**
	 * This method is called when player picked a card.
	 * The card value is subtracted from the player total number of points.
	 * 
	 * @param player  the player that picked the card.
	 * 
	 */
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
