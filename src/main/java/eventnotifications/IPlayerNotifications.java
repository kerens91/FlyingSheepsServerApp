package eventnotifications;

public interface IPlayerNotifications {
	public void playerHandCardRemoved(String playerId, int cardId);
	public void playerHandCardAdded(String playerId, int cardId);
	public void playerHandUpdatePoints(String playerId, int numOfPoints);
}
