package eventnotifications;

import clientservershared.CardModel;
import clientservershared.GameOver;

public interface IGameNotifications {
	public void onNumberOfActivePlayersChanged(int numOfPlayers);
	public void onCurrentPlayerChanged(String currentPlayer);
	public void onCardUsed(CardModel card);
	public void onPlayerWinGame(GameOver info);
	public void onPlayerLostGame(String playerId);
}
