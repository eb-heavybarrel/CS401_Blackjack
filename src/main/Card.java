package main;

public class Card {
	private CardSuit cardSuit;
	private CardValue cardValue;

	public Card(CardSuit cardSuit, CardValue cardValue) {
		this.cardSuit = cardSuit;
		this.cardValue = cardValue;
	}

	public CardSuit getSuit() {
		return this.cardSuit;
	}

	public CardValue getValue() {
		return this.cardValue;
	}
	
	@Override() 
	public String toString(){
		return (cardValue.getValueString() + "_of_" + cardSuit.getSuitName()).toLowerCase();
	}
}