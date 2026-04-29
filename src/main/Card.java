package main;

public class Card {
	private CardSuit cardSuit;
	private CardValue cardValue;
	private CardLocation location;
	
	public Card (CardSuit cardSuit, CardValue cardValue) {
		this.cardSuit = cardSuit;
		this.cardValue = cardValue;
		this.location = CardLocation.SHOE;
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
	public CardLocation Location (CardLocation location){
		return this.location = location;
	}
	
}