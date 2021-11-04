package attackstate.interfaces;

import card.AbstractCard;

public interface IAttackDefensable extends IAttackFailable {
	Boolean defenseSucceeded(AbstractCard defenseCard);
}
