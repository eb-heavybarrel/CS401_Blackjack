package main;

import java.util.ArrayList;

public class Hand {
	private ArrayList<Card> cards;

	private int bet;
	private int handValue;
	
	public void placeBet(int bet) {
		this.bet = bet;
	}
	
	public Hand() {
		cards = new ArrayList<Card>();
	}
	
	public int getHandValue() {
		return handValue;
	}

	public void resetHand() {
		handValue = 0;
		cards.clear();
	}
	public void addCard(Card card) {
		cards.add(card);
		handValue += card.getValue().getValueInt();
	}
}
