package clientservershared;

import game.players.Player;

public class PlayerModel {
	private String playerId;
    private String name;
    private int score;
    private String img;
    private Boolean isActive;
    private Boolean myTurn;

    // remove
    public PlayerModel(String playerId, int score, String name, String img, Boolean isActive ) {
        this.playerId = playerId;
        this.name = name;
        this.img = img;
        this.score = score;
        this.isActive = isActive;
        this.myTurn = false;
    }
    
    public PlayerModel(Player player, String currentPlayer) {
        playerId = player.getId();
        name = player.getName();
        img = player.getImg();
        score = player.getPlayerScore();
        isActive = player.isActive();
        myTurn = currentPlayer == playerId ? true : false;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getImg() {
        return img;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean state) {
        isActive = state;
    }
    
    public Boolean getMyTurn() {
        return myTurn;
    }

    public void setMyTurn(Boolean state) {
        myTurn = state;
    }
}
