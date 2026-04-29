package main;

public class Card {
	private CardSuit cardSuit;
	private CardValue cardValue;
	private CardLocation location;
	
	public Card (CardSuit cardSuit, CardValue cardValue, CardLocation location) {
		this.cardSuit = cardSuit;
		this.cardValue = cardValue;
		this.location = location;
	}
	
	public CardSuit getSuit() {
		return this.cardSuit;
	}
	public CardValue getValue() {
		return this.cardValue;
	}
	public CardLocation getLocation() {
		return this.location;
	}
}