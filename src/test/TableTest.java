package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.Table;

class TableTest {

	@Test
	void testTableInsertion() {
		Table table = new Table();
		
		table.PlayerJoin("Paul");
		table.PlayerJoin("Nick");
		table.PlayerJoin("Manuel");
		
		assertEquals(table.getPlayerCount(), 3);
	}
	
	@Test
	void testTableIsFull() {
		Table table = new Table();
		
		table.PlayerJoin("Paul");
		table.PlayerJoin("Nick");
		table.PlayerJoin("Manuel");
		table.PlayerJoin("Kevin");
		table.PlayerJoin("David");
		table.PlayerJoin("Aaron");
		
		assertTrue(table.isFull());
	}
	
	@Test
	void testPlayerRemoval() {
		Table table = new Table();
		
		table.PlayerJoin("Paul");
		table.PlayerJoin("Nick");
		table.PlayerJoin("Manuel");
		table.PlayerJoin("Kevin");
		table.PlayerJoin("David");
		table.PlayerJoin("Aaron");
		
		table.PlayerLeave("Manuel");
		
		assertEquals(table.getPlayerCount(), 5);
		assertNull(table.getPlayer("Manuel"));
	}
	
	@Test
	void testFirstJoinEmptySeat() {
		Table table = new Table();
		
		table.PlayerJoin("Paul");
		table.PlayerJoin("Nick");
		table.PlayerJoin("Manuel");
		table.PlayerJoin("Kevin");
		table.PlayerJoin("David");
		table.PlayerJoin("Aaron");
		
		table.PlayerLeave("Manuel");
		table.PlayerLeave("Aaron");
		
		table.PlayerJoin("Sarah");
		assertEquals(table.getPlayer(2).UserName(), "Sarah");
	}

}
