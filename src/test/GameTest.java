package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.Table;
import main.Game;

class GameTest {

	@Test
	void testHitCommand() {
		Table table = new Table();
		Game game = new Game(table);
		
		table.PlayerJoin("Manuel");
		
		game.hit(0);
		
		assertTrue(table.getPlayer("Manuel").getHand().getHandValue() >= 2);
	}

}
