package card.types;

import static globals.Constants.CARD_END_TURN;

import card.AbstractCard;
import clientservershared.CardModel;
import game.players.Player;

/**
* Abstract Playable Card class defines the cards that can be played by players.
* A player can choose to use the card by picking it in his turn.
*  
* All cards in the game are playable cards, except the nature disaster cards.
* 
* This class inherits from the basic AbstractCard class.
* 
* once a player pick a playable card, the card is set as used,
* removed from player hand, displayed in the used cards pile,
* and the card functionality is activated, depends on the card type.
* 
* The playable card is presented in the game screen, each player can see his card,
* thus the playable card has decorative fields that the basic card don't have.
* 
* @author      Keren Solomon
*/
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

	/**
	 * This method is called when a player draws this card from the deck.
	 * The card is added to player hand.
	 * 
	 * This method indicates that the current player finished his turn by getting
	 * a card from the deck.
	 * 
	 * @param player  the player that got this card from deck.
	 * 
	 * @return  true|false	true if the turn ended, otherwise false.
	 */
	public Boolean playCardFromDeck(Player player) {
		player.addCardToPlayerHand(this);
		return CARD_END_TURN;
	}
	
	/**
	 * This method indicated that the card can be used by players.
	 * 
	 * @return  true|false	true if the card can be played by player, otherwise false.
	 * 
	 */
	@Override
	public Boolean isPlayable() {
		return true;
	}
	
	/**
	 * This method obligates the inheritance classes to implement the handling method
	 * for a case that player picked a card.
	 *  
	 */
	public abstract void playerPickedCard(Player player);
	
	/**
	 * This method is called player picked 2 cards.
	 * The cards are set as used.
	 * 
	 * @param player  the player that picked the cards.
	 * @param card    the second card in the couple (first is this class card).
	 * 
	 */
	public void playerPickedCouple(Player player, AbstractPlayableCard card) {
		this.setCardUsed(player);
		card.setCardUsed(player);
	}
	
	/**
	 * This method is called when player picked a card.
	 * The card is set as used.
	 * 
	 * @param player  the player that picked the card.
	 * 
	 */
	public void setCardUsed(Player player) {
		player.removeCardFromPlayerHand(this);
	}
	
	/**
	 * This method is called when the server need to send the card information to the client,
	 * thus using the shared structure CardModel containing the card information.
	 * A new CardModel is created with the card information.
	 * 
	 * @return  CardModel	card information as a CardModel object
	 * 
	 */
	@Override
	public CardModel getCardInfo() {
		return new CardModel(getId(), getName(), getImg(), textColor, frame, back, null);
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
}
