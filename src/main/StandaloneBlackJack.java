package main;

import java.util.Scanner;

public class StandaloneBlackJack {
	public static void main(String[] args) {

		Table table = new Table();
		Game game = table.getGame();

		table.PlayerJoin("Manuel");
		table.PlayerJoin("Paul");
		// table.PlayerJoin("Nick");

		// Betting Round
		
		// Game Loop
		game.play();
	}
}
