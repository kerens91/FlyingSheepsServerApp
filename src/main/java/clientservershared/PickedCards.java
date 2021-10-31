package clientservershared;

public class PickedCards {
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
}
