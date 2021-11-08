package game;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameTest {

	@Test
	public void startGame() {
		int numOfPlayers = 2;
		Game game = new Game();
		game.createNewGame(numOfPlayers);
		fail("Failed to create a new game");
	}

}
