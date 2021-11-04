package eventnotifications;

public interface IPlayerNotifications {
	void playerHandCardRemoved(String playerId, int cardId);
	void playerHandCardAdded(String playerId, int cardId);
	void playerHandUpdatePoints(String playerId, int numOfPoints);
}
