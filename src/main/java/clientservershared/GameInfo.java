package clientservershared;

import java.util.List;


public class GameInfo {
	private List<PlayerModel> players;
	private List<CardModel> cards;
	private PlayerModel me;
	
	public void setMyPlayer(PlayerModel me) {
		this.me = me;
	}
	
	public PlayerModel getMyPlayer() {
		return me;
	}

	public List<PlayerModel> getPlayers() {
		return players;
	}

	public List<CardModel> getCards() {
		return cards;
	}

	public void setPlayers(List<PlayerModel> players) {
		this.players = players;
	}

	public void setCards(List<CardModel> cards) {
		this.cards = cards;
	}
}
