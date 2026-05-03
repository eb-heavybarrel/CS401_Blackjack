package main;

import java.util.*;

public class Shoe {

	private ArrayDeque<Card> cards;
	private int shoeSize;
	private int cardsRemaining;
	private int triggerShuffleSize = 60;

	public Shoe(int shoeSize) {
		this.shoeSize = shoeSize;
		int totalCards = shoeSize * 52;

		cardsRemaining = totalCards;
		generateDeck();
	}

	private void shuffle() {
		ArrayList<Card> tempStack = new ArrayList<Card>(cards);
		Collections.shuffle(tempStack);
		cards = new ArrayDeque<Card>(tempStack);
	}

	private void generateDeck() {
		cards = new ArrayDeque<Card>();
		
		for (int k = 0; k < shoeSize; k++) {
			// Loop for suit
			for (int i = 0; i < CardSuit.values().length; i++) {
				// Loop for value
				for (int j = 0; j < 13; j++) {
					Card tempCard = new Card(CardSuit.values()[i], CardValue.values()[j]);
					cards.push(tempCard);
				}
			}
		}
		shuffle();
	}

	public Card deal() {
		if (cards.size() < triggerShuffleSize) {
			generateDeck();
			this.cardsRemaining = cards.size();
		}
		cardsRemaining--;
		return cards.pop();
	}
}
