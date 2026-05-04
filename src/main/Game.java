package main;

import java.util.Scanner;

public class Game {
	Scanner sc = new Scanner(System.in);

	private Table table;
	private Hand dealerHand;
	private Shoe shoe;

	// Flags
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

		// Bet Loop
		for (int k = 0; k < table.getPlayerCount(); k++) {
			String name = table.getPlayer(k).UserName();
			System.out.println(table.getMinBet() + " is the minimum, " + table.getMaxBet() + " is the maximum");
			System.out.println("Enter bet amount: ");

			int betAmount = sc.nextInt();

			canBet = false;
			while (!canBet) {
				bet(k, betAmount);
				canBet = true;
				continue;
			}
		}

		// Player Loop
		for (int i = 0; i < table.getPlayerCount(); i++) {
			stand = false;
			while (!stand) {
				Hand hand = table.getPlayer(i).getHand();
				int handValue = hand.getHandValue();
				String name = table.getPlayer(i).UserName();

				if (handValue > 21) {
					System.out.println(name + " Busted!");
					System.out.println(name + "'s final hand value: " + handValue);
					hand.setHandState(HandState.LOSER);
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

		// Dealer Loop
		while (!dealerStand) {
			dealerMenu();
		}

		determineWinner();
	}

	private void hit(int seatIndex) {
		Card card = shoe.deal();

		if (seatIndex > -1) {
			Hand hand = table.getPlayer(seatIndex).getHand();
			String name = table.getPlayer(seatIndex).UserName();

			hand.addCard(card);
			System.out.println(name + "'s current hand value: " + hand.getHandValue());
		} else {
			dealerHand.addCard(card);
			System.out.println("Dealer's current hand value: " + dealerHand.getHandValue());
		}
	}

	private void bet(int seatIndex, int betAmount) {
		// Initial Bets
		Hand playerHand = table.getPlayer(seatIndex).getHand();
		String name = table.getPlayer(seatIndex).UserName();
		int bet;

		if (betAmount < table.getMinBet()) {
			bet = table.getMinBet();
		} else if (betAmount > table.getMaxBet()) {
			bet = table.getMaxBet();
		} else {
			bet = betAmount;
		}

		playerHand.placeBet(bet);
		System.out.println(name + " placed a bet of " + bet);
	}

	private void doubleDown(int seatIndex) {
		String name = table.getPlayer(seatIndex).UserName();
		Hand playerHand = table.getPlayer(seatIndex).getHand();
		int initialBet = playerHand.getBet();

		int doubledBet = initialBet * 2; // doubles initial bet
		playerHand.placeBet(doubledBet);
		System.out.println(name + " has doubled down! Bet is now at " + playerHand.getBet());

		hit(seatIndex); // recieve addition card

		stand = true; // stand
	}

	private void playerMenu(int seatIndex) {
		// Input Receivers
		String[] commands = { "Hit", "Stand", "Double Down" };
		int input;

		String name = table.getPlayer(seatIndex).UserName();
		int handValue = table.getPlayer(seatIndex).getHand().getHandValue();

		System.out.println(name + "'s Turn!");
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
			System.out.println(name + "'s final hand value: " + handValue);
			stand = true;
			break;
		case 2:
			doubleDown(seatIndex);
			break;
		default:
			System.out.println("Invalid command. Automatically selecting stand \n");
			break;
		}
	}

	private void dealerMenu() {
		// Input Receivers
		String[] commands = { "Hit", "Stand" };
		int input;

		if (dealerHand.getHandValue() > 21) { // Dealer's hand was a bust
			System.out.println("Dealer Busted!");
			System.out.println("Dealer's final hand value: " + dealerHand.getHandValue());
			dealerHand.setHandState(HandState.LOSER);
			dealerStand = true;
			return;
		} else if (dealerHand.getHandValue() <= 21 && dealerHand.getHandValue() > 16) { // Dealer can't hit on a soft 17
			System.out.println("Dealer's final hand value: " + dealerHand.getHandValue());
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
			System.out.println("Dealer's final hand value: " + dealerHand.getHandValue());
			dealerStand = true;
			break;
		default:
			System.out.println("Invalid command. Automatically selecting stand \n");
			break;
		}
	}

	// Once everyone plays their turn
	private void determineWinner() {
		for (int i = 0; i < table.getPlayerCount(); i++) {
			Hand hand = table.getPlayer(i).getHand();
			String player = table.getPlayer(i).UserName();

			if (dealerHand.hasBlackJack() && !hand.hasBlackJack()) {
				hand.setHandState(HandState.LOSER);
				System.out.println(player + ": Loser");
			} else if (dealerHand.hasBlackJack() && hand.hasBlackJack()) {
				hand.setHandState(HandState.PUSH);
				dealerHand.setHandState(HandState.PUSH);
				System.out.println(player + ": Push");
				System.out.println("Dealer: Push");
			} else if (hand.hasBlackJack() && !dealerHand.hasBlackJack()) {
				hand.setHandState(HandState.WINNER);
				dealerHand.setHandState(HandState.LOSER);
				System.out.println(player + ": Winner");
				System.out.println("Dealer: Loser");
			} else if (dealerHand.getHandValue() > hand.getHandValue() && dealerHand.getHandState() != HandState.LOSER) {
				hand.setHandState(HandState.LOSER);
				System.out.println(player + ": Loser");
			} else if (hand.getHandValue() > dealerHand.getHandValue() && hand.getHandState() != HandState.LOSER) {
				hand.setHandState(HandState.WINNER);
				dealerHand.setHandState(HandState.LOSER);
				System.out.println(player + ": Winner");
				System.out.println("Dealer: Loser");
			} else if (hand.getHandValue() == dealerHand.getHandValue()) {
				hand.setHandState(HandState.PUSH);
				dealerHand.setHandState(HandState.PUSH);
				System.out.println(player + ": Push");
				System.out.println("Dealer: Push");
			} else if (hand.getHandState() != HandState.WINNER && hand.getHandState() != HandState.PUSH) {
				hand.setHandState(HandState.LOSER);
				System.out.println("Dealer: Loser");
			}
			if (dealerHand.getHandState() != HandState.PUSH && dealerHand.getHandState() != HandState.LOSER) {
				dealerHand.setHandState(HandState.WINNER);
				System.out.println("Dealer: Winner");
			}
		}
	}

	public void testDetermineWinner() {
		determineWinner();
	}
}
