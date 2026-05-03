package main;

public enum CardSuit {
	SPADES("Spades", 0), 
	HEARTS("Hearts", 1), 
	DIAMONDS("Diamonds",2), 
	CLUBS("Clubs", 3);
	
	String suitName;
	int suitIndex;

	CardSuit(String suitName,int suitIndex) {
		this.suitName = suitName;
		this.suitIndex = suitIndex;
	}
	
	public String getSuitName() {
		return suitName;
	}
	
	public int getSuitIndex() {
		return suitIndex;
	}
}
