package card.implementation.regular;


import card.AbstractCard;
import card.CardFactory;
import card.interfaces.ICoupledCard;
import card.types.AbstractPlayableCard;
import card.types.AbstractValueableCard;
import game.player.Player;


public class RegularCard extends AbstractValueableCard implements ICoupledCard {
	public RegularCard(int typeId, int gameId, String name, int txtColor, String img, String frame, String back, int value, String points) {
		super(typeId, gameId, name, txtColor, img, frame, back, value, points);
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
	
	@Override
	public Boolean isCouple(AbstractCard card) {
		if ((card instanceof RegularCard) && (card.getType() == this.getType())) {
			return true;
		}
		return false;
	}
	
	@Override
	public void doCouple(Player player) {
		System.out.println("couple cards");
		AbstractCard steal = CardFactory.getInstance().getStealCard();
		cardNotifications.attackCardPicked(steal, player);
	}
}
