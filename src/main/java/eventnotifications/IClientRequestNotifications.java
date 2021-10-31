package eventnotifications;

import clientservershared.PickedCards;

public interface IClientRequestNotifications {
	public void onNewGameRequest(String clientId, int numOfPlayers);
	public void onJoinGameRequest(String clientId, String password, String name, String img);

	public void onDealCardReq(String clientId);
	public void onPlayerPickedCards(String clientId, PickedCards cards);

	public void onAttackPlayerReq(String victimId);
	public void onPlayerLostAttack(String clientId);
}
