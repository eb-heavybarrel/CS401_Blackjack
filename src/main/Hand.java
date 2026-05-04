package main;

import java.util.ArrayList;

public class Hand {
	private ArrayList<Card> cards;
	private HandState handState;
	private int bet;
	private int handValue;
	private int aceCount;

	public void placeBet(int bet) {
		this.bet = bet;
	}

	public Hand() {
		cards = new ArrayList<Card>();
		handValue = 0;
		bet = 0;
	}

	public int getHandValue() {
		return handValue;
	}
	
	public int getBet() {
		return bet;
	}
	public HandState getHandState (){
		return handState;
	}
	
	public boolean hasBlackJack() {
		return cards.size() == 2 && handValue == 21;
	}

	public void resetHand() {
		handValue = 0;
		cards.clear();
	}

	public void addCard(Card card) {
		cards.add(card);

		updateHandValue();
	}
	
	public void setHandState(HandState state) {
		handState = state;
	}

	private void updateHandValue() {
		aceCount = 0;
		int modifiedAces = 0;
		handValue = 0;

		// Interate through each card in hand
		for (Card c : cards) {
			int cardValue = c.getValue().valueInt;

			// Checks for ace
			if (cardValue == 11) {
				aceCount++;
			}
			handValue += cardValue;
		}
		while (handValue > 21 && aceCount > 0) {
			modifiedAces++;
			handValue -= 10;

			if (modifiedAces == aceCount) {
				return;
			}
		}
	}
}
