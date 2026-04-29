package main;

public enum CardValue {
	TWO ("two", 2),
	THREE ("three", 3),
	FOUR ("four", 4),
	FIVE ("five", 5),
	SIX ("six", 6),
	SEVEN ("seven", 7),
	EIGHT ("eight", 8),
	NINE ("nine", 9),
	TEN ("ten", 10),
	JACK ("j", 10),
	QUEEN ("q", 10),
	KING ("k", 10), 
	ACE ("a", 11);
	
	String sVal;
	int iVal;
	
	CardValue(String sVal, int iVal) {
		this.sVal = sVal;
		this.iVal = iVal;
	}
}
