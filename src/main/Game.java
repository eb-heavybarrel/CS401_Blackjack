package main;

import java.util.Scanner;

public class Game {
	private Table table;

	private Hand dealerHand;

	private Shoe shoe;
	public boolean canBet;
	private boolean stand = false;
	private boolean dealerStand = false;

	public Game(Table table) {
		this.table = table;
		this.shoe = table.getShoe();

		dealerHand = new Hand();
	}

	public Hand DealerHand() {
		return dealerHand;
	}

	private void initialDeal() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < table.getPlayerCount(); j++) {
				hit(j); // Player dealt
			}

			hit(-1); // Dealer dealt
		}
	}

	public void play() {
		initialDeal();

		for (int i = 0; i < table.getPlayerCount(); i++) {
			stand = false;
			while (!stand) {
				int handValue = table.getPlayer(i).getHand().getHandValue();

				if (handValue > 21) {
					System.out.println(table.getPlayer(i).UserName() + " Busted!");
					stand = true;
					continue;
				}
				if (handValue == 21) {
					stand = true;
					continue;
				}

				playerMenu(i);
			}
		}
		
		while (!dealerStand) {
			dealerMenu();
		}

	}

	private void hit(int seatIndex) {
		Card card = shoe.deal();

		if (seatIndex > -1) {
			table.getPlayer(seatIndex).getHand().addCard(card);
			System.out.println(table.getPlayer(seatIndex).UserName() + "'s current hand value: "
					+ table.getPlayer(seatIndex).getHand().getHandValue());
		} else {
			dealerHand.addCard(card);
			System.out.println("Dealer's current hand value: " + dealerHand.getHandValue());
		}
	}

	private void bet() {
		// Initial Bets
	}

	private void playerMenu(int seatIndex) {
		// Input Receivers
		Scanner sc = new Scanner(System.in);
		String[] commands = { "Hit", "Stand" };
		int input;

		System.out.println(table.getPlayer(seatIndex).UserName() + "'s Turn!");
		System.out.println("Select an option: ");
		for (int i = 0; i < commands.length; i++) {
			System.out.println(i + ") " + commands[i]);
		}
		input = sc.nextInt();
		switch (input) {
		case 0:
			hit(seatIndex);
			break;
		case 1:
			stand = true;
			break;
		default:
			System.out.println("Invalid command. Automatically selecting stand \n");
			break;
		}
	}

	private void dealerMenu() {
		// Input Receivers
		
		
		Scanner sc = new Scanner(System.in);
		String[] commands = { "Hit", "Stand" };
		int input;
		
		// Dealer can't hit on a soft 17
		if (dealerHand.getHandValue() > 16) {
			dealerStand = true;
			return;
		}
		
		System.out.println("Dealer's Turn!");
		System.out.println("Select an option: ");
		for (int i = 0; i < commands.length; i++) {
			System.out.println(i + ") " + commands[i]);
		}
		input = sc.nextInt();
		switch (input) {
		case 0:
			hit(-1);
			break;
		case 1:
			dealerStand = true;
			break;
		default:
			System.out.println("Invalid command. Automatically selecting stand \n");
			break;
		}
	}

	// Once everyone play's their turn
	private void determineWinner() {

	}
}
