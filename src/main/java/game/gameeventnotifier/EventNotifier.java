package game.gameeventnotifier;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import clientservershared.CardModel;
import clientservershared.GameOver;
import eventnotifications.IGameNotifications;
import game.GameManager;

public class EventNotifier {
	private static final Logger logger = LogManager.getLogger(EventNotifier.class);
	private IGameNotifications gameNotificationsCallback;

	public EventNotifier() {
		gameNotificationsCallback = (IGameNotifications)GameManager.getInstance();
	}
	
	private void notifyGameEvent(Runnable callback) {
		Optional.ofNullable(gameNotificationsCallback)
			.ifPresentOrElse(
					notificationsCallback -> callback.run(), 
					() -> logger.error("callback is null"));
    }
	
	public void activePlayers(int numPlayers) {
		notifyGameEvent(() -> gameNotificationsCallback.onNumberOfActivePlayersChanged(numPlayers));
    }
    
	public void cardUsed(CardModel card) {
		notifyGameEvent(() -> gameNotificationsCallback.onCardUsed(card));
    }
    
	public void endTurn(String currentPlayer) {
		notifyGameEvent(() -> gameNotificationsCallback.onCurrentPlayerChanged(currentPlayer));
    }
    
	public void gameOver(GameOver gameOverInfo) {
		notifyGameEvent(() -> gameNotificationsCallback.onPlayerWinGame(gameOverInfo));
    }
    
	public void lostGame(String playerId) {
		notifyGameEvent(() -> gameNotificationsCallback.onPlayerLostGame(playerId));
	}
}
