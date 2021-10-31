package attackstate.interfaces;

import card.AbstractCard;

public interface IAttackDefensable extends IAttackFailable {
	public abstract Boolean defenseSucceeded(AbstractCard defenseCard);
}
