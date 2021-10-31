package card.implementation.ndisaster;

import java.util.List;

import card.AbstractCard;
import card.interfaces.IDefenseCard;
import card.types.AbstractNDisasterCard;
import database.entity.StringEntity;

public class PitCard extends AbstractNDisasterCard {
	public PitCard(int typeId, int gameId, String name, String img, List<StringEntity> strings) {
		super(typeId, gameId, name, img, strings);
	}

	@Override
	public Boolean defenseSucceeded(AbstractCard defenseCard) {
		if (((IDefenseCard) defenseCard).isPitDefenseCard()) {
			return true;
		}
		return false;
	}
}
