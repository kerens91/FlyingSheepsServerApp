package eventnotifications;

import clientservershared.CardModel;
import clientservershared.GameOver;

public interface IGameNotifications {
	void onNumberOfActivePlayersChanged(int numOfPlayers);
	void onCurrentPlayerChanged(String currentPlayer);
	void onCardUsed(CardModel card);
	void onPlayerWinGame(GameOver info);
	void onPlayerLostGame(String playerId);
}
