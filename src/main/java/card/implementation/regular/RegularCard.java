package card.implementation.regular;


import card.AbstractCard;
import card.CardFactory;
import card.interfaces.ICoupledCard;
import card.types.AbstractPlayableCard;
import card.types.AbstractValueableCard;
import database.entity.CardEntity;
import game.players.Player;

/**
* This class represents a regular Card.
* It means that the card is not an attack card nor a defense card.
* There are 15 types of regular cards in the game:
* <ul>
* <li>Drunk Sheep
* <li>Baby Sheep
* <li>Fat Sheep
* <li>Rockstar Sheep
* <li>Athlete Sheep
* <li>Grumpy Sheep
* <li>Chef Sheep
* <li>Golden Sheep
* <li>Sexy Sheep
* <li>Nerdy Sheep
* <li>Winter Sheep
* <li>Summer Sheep
* <li>Tired Sheep
* <li>Fireman Sheep
* <li>Doctor Sheep
* </ul>
* Each of the types have a different value.
* 
* This class inherits from the AbstractValueableCard class, it means that the card has a value,
* thus increasing the number of points of the player that holds that card.
* Also, a valuable card is a playable card, thus, the card can be played by the player (not only by the deck),
* a player can pick and play the card during the game.
* This class implements the ICoupledCard interface, that gives the card a different meaning when used as a coupled -
* 
* The card alone is regular card, but when picked couple of same type - this is a steal attack.
* In this type of attack, a player is attacking another player, try to steal one of his cards.
* 
* @author      Keren Solomon
*/
public class RegularCard extends AbstractValueableCard implements ICoupledCard {
	
	public RegularCard(CardEntity cEntity, int gameId) {
		super(cEntity.getId(),
				gameId,
				cEntity.getName(),
				cEntity.getDecore().getTxtCol(),
				cEntity.getDecore().getImg(),
				cEntity.getDecore().getFrameImg(),
				cEntity.getDecore().getBackImg(),
				cEntity.getValue(),
				cEntity.getDecore().getPointsImg());
	}

	/**
	 * This method is called when a player picked a regular card.
	 * The card is marked as used, meaning removed from player hand.
	 * 
	 * @param player  the Player that picked the card.
	 * 
	 */
	@Override
	public void playerPickedCard(Player player) {
		setCardUsed(player);		
	}

	/**
	 * This method is called when a player picked 2 cards.
	 * Both cards are marked as used, meaning removed from player hand.
	 * Check if this is couple are matching couple - call do couple method.
	 * 
	 * @param player  the Player that picked the cards.
	 * @param card    the second card in the couple (first is this class card).
	 * 
	 */
	@Override
	public void playerPickedCouple(Player player, AbstractPlayableCard card) {
		super.playerPickedCouple(player, card);
		if (isCouple(card)) {
			doCouple(player);
		}	
	}
	
	/**
	 * This method indicates that the cards are matching couple -
	 * check that both are regular cards, and that both are the same type.
	 * 
	 * @param card    the second card in the couple (first is this class card).
	 * 
	 * @return  true|false	true if the cards are matching couple, otherwise false.
	 */
	@Override
	public Boolean isCouple(AbstractCard card) {
		return ((card instanceof RegularCard) && (card.getType() == this.getType()));
	}
	
	/**
	 * This method is called when there are 2 matching regular cards picked by a player.
	 * In this case, the player is starting an attack - steal attack.
	 * 
	 * Get a new card of type Steal card from cards factory, this card will constitute the attack card.
	 * Call an event to notify the GameManager class about the attack.
	 * 
	 * @param player  the Player that picked the cards.
	 * 
	 */
	@Override
	public void doCouple(Player player) {
		AbstractCard steal = CardFactory.getInstance().getStealCard();
		cardNotifications.attackCardPicked(steal, player);
	}
}
