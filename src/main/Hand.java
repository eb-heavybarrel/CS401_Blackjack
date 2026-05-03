package main;

import java.util.ArrayList;

public class Hand {
	private ArrayList<Card> cards;
	private CardLocation player;

	private int bet;
	private int handValue;

	public Hand(CardLocation player) {
		this.player = player;
		cards = new ArrayList<Card>();
	}

	public void placeBet() {

	}

	public void playHand() {

	}
	
	public int getHandValue() {
		return handValue;
	}

	public void resetHand() {
		handValue = 0;
		cards.clear();
	}
}
