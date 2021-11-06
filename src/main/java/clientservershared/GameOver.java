package clientservershared;

import game.gameplayers.Player;

public class GameOver {
	private String winnerId;
	private String winnerName;
	private int winnerPoints;
	private WinType winReason;
	
	public GameOver() {	}
	
	public WinType getWinReason() {
		return winReason;
	}
	
	public void setWinReason(WinType winReason) {
		this.winReason = winReason;
	}

	public void setWinner(Player winner) {
		this.winnerId = winner.getId();
		this.winnerName = winner.getName();
		this.winnerPoints = winner.getPlayerScore();
	}
	
	public String getWinnerId() {
		return winnerId;
	}

	public String getWinnerName() {
		return winnerName;
	}

	public int getWinnerPoints() {
		return winnerPoints;
	}

	public enum WinType {
		WinType_Points,
		WinType_cards,
		WinType_last
	}
}
