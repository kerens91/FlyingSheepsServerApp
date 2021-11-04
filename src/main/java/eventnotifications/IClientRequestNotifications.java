package eventnotifications;

import clientservershared.PickedCards;

public interface IClientRequestNotifications {
	void onNewGameRequest(String clientId, int numOfPlayers);
	void onJoinGameRequest(String clientId, String password, String name, String img);

	void onDealCardReq(String clientId);
	void onPlayerPickedCards(String clientId, PickedCards cards);

	void onAttackPlayerReq(String victimId);
	void onPlayerLostAttack(String clientId);
}
