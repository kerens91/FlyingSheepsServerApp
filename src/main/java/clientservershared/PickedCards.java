package clientservershared;

import static globals.Constants.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PickedCards {
	private static final Logger logger = LogManager.getLogger(PickedCards.class);
	private final int MIN_PICKED_CARDS 		= 0;
	private final int INVALID_CARD_IDX 		= -1;
	
	private int card1;
	private int card2;
	private int numPickedCards;
	
	public PickedCards() {
		card1 = INVALID_CARD_IDX;
		card2 = INVALID_CARD_IDX;
		numPickedCards = MIN_PICKED_CARDS;
	}

	public int getCard1() {
        return card1;
    }

    public int getCard2() {
        return card2;
    }

    public int getNumOfPickedCards() {
        return numPickedCards;
    }
    
    public void handlePickedCards(Runnable handleSingleCard, Runnable handleDoubleCards) {
		switch (numPickedCards) {
			case NO_PICKED_CARDS:
				logger.error("No cards picked");
				break;
			case SINGLE_PICKED_CARD:
				handleSingleCard.run();
				break;
			case COUPLE_PICKED_CARDS:
				handleDoubleCards.run();
				break;

			default:
				break;
		}
	}
}
